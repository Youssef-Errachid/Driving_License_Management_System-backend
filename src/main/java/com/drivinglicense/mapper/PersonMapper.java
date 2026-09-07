package com.drivinglicense.mapper;

import com.drivinglicense.dto.person.PersonCreateDTO;
import com.drivinglicense.dto.person.PersonResponseDTO;
import com.drivinglicense.dto.person.PersonUpdateDTO;
import com.drivinglicense.entity.Person;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    Person toEntity(PersonCreateDTO dto);

    PersonResponseDTO toResponseDTO(Person person);

    List<PersonResponseDTO> toResponseDTOList(List<Person> persons);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(PersonUpdateDTO dto, @MappingTarget Person person);
}