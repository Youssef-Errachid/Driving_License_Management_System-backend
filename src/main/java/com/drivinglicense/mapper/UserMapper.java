package com.drivinglicense.mapper;

import com.drivinglicense.dto.user.UserCreateDTO;
import com.drivinglicense.dto.user.UserResponseDTO;
import com.drivinglicense.dto.user.UserUpdateDTO;
import com.drivinglicense.entity.Person;
import com.drivinglicense.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserCreateDTO dto);

    default UserResponseDTO toResponseDTO(User user){
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setUserStatus(user.getUserStatus());
        dto.setCreationDate(user.getCreationDate());

        Person person = user.getPerson();
        if(person != null){
            dto.setPersonId(person.getId());
            dto.setPersonFullName(person.getFirstName() + " " + person.getLastName());
        }
        return dto;
    }

    List<UserResponseDTO> toResponseDTOList(List<User> users);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(UserUpdateDTO dto, @MappingTarget User user);

}