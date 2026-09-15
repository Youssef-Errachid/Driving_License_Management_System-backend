package com.drivinglicense.service.impl;

import com.drivinglicense.dto.common.PageResponseDTO;
import com.drivinglicense.dto.person.PersonCreateDTO;
import com.drivinglicense.dto.person.PersonResponseDTO;
import com.drivinglicense.dto.person.PersonUpdateDTO;
import com.drivinglicense.entity.Person;
import com.drivinglicense.exception.BusinessException;
import com.drivinglicense.exception.ResourceNotFoundException;
import com.drivinglicense.mapper.PersonMapper;
import com.drivinglicense.repository.PersonRepository;
import com.drivinglicense.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheEvict;


import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    @Override
    @Transactional
    public PersonResponseDTO create(PersonCreateDTO dto) {
        if (personRepository.existsByNationalNumber(dto.getNationalNumber())) {
            throw new BusinessException("a person with this national number already exists");
        }
        Person person = personMapper.toEntity(dto);
        Person saved = personRepository.save(person);
        return personMapper.toResponseDTO(saved);
    }

    @Override
    public PersonResponseDTO getById(Long id) {
        return personMapper.toResponseDTO(findPersonOrThrow(id));
    }

    @Override
    public PageResponseDTO<PersonResponseDTO> getAll(int page, int size) {
        Page<Person> result = personRepository.findAll(PageRequest.of(page, size));
        List<PersonResponseDTO> content = personMapper.toResponseDTOList(result.getContent());
        return new PageResponseDTO<>(content, result.getNumber(), result.getTotalPages(), result.getTotalElements());
    }

    @Override
    public List<PersonResponseDTO> search(String query) {
        return personMapper.toResponseDTOList(personRepository.search(query));
    }

    @Override
    @Transactional
    @CacheEvict(value = "drivers", allEntries = true)
    public PersonResponseDTO update(Long id, PersonUpdateDTO dto) {
        Person person = findPersonOrThrow(id);
        personMapper.updateEntityFromDTO(dto, person);
        return personMapper.toResponseDTO(person);
    }

    private Person findPersonOrThrow(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person", id));
    }
}