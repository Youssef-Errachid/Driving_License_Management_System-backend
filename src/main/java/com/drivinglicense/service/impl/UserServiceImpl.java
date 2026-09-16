package com.drivinglicense.service.impl;

import com.drivinglicense.dto.common.PageResponseDTO;
import com.drivinglicense.dto.user.UserCreateDTO;
import com.drivinglicense.dto.user.UserResponseDTO;
import com.drivinglicense.dto.user.UserUpdateDTO;
import com.drivinglicense.entity.Person;
import com.drivinglicense.entity.User;
import com.drivinglicense.enums.UserStatus;
import com.drivinglicense.exception.BusinessException;
import com.drivinglicense.exception.ResourceNotFoundException;
import com.drivinglicense.mapper.UserMapper;
import com.drivinglicense.repository.PersonRepository;
import com.drivinglicense.repository.UserRepository;
import com.drivinglicense.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public UserResponseDTO create(UserCreateDTO dto) {
        Person person = personRepository.findById(dto.getPersonId())
                .orElseThrow(() -> new ResourceNotFoundException("Person", dto.getPersonId()));

        if (person.getUser() != null) {
            throw new BusinessException("this person already has a user account");
        }

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new BusinessException("a user with this email already exists");
        }

        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPerson(person);
        user.setUserStatus(UserStatus.ACTIVE);

        User saved = userRepository.save(user);
        return userMapper.toResponseDTO(saved);
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public UserResponseDTO getById(Long id) {
        return userMapper.toResponseDTO(findUserOrThrow(id));
    }

    @Override
    @Cacheable(value = "users", key = "'page:' + #page + ':size:' + #size")
    public PageResponseDTO<UserResponseDTO> getAll(int page, int size) {
        Page<User> result = userRepository.findAll(PageRequest.of(page, size));
        List<UserResponseDTO> content = userMapper.toResponseDTOList(result.getContent());
        return new PageResponseDTO<>(content, result.getNumber(), result.getTotalPages(), result.getTotalElements());
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public UserResponseDTO update(Long id, UserUpdateDTO dto) {
        User user = findUserOrThrow(id);
        userMapper.updateEntityFromDTO(dto, user);
        return userMapper.toResponseDTO(user);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public void delete(Long id) {
        userRepository.delete(findUserOrThrow(id));
    }

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }
}