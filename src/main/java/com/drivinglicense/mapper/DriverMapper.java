package com.drivinglicense.mapper;

import com.drivinglicense.dto.driver.DriverResponseDTO;
import com.drivinglicense.entity.Driver;
import com.drivinglicense.entity.Person;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DriverMapper {
    default DriverResponseDTO toResponseDTO(Driver driver){
       DriverResponseDTO dto = new DriverResponseDTO();
       dto.setId(driver.getId());
       dto.setDriverNumber(driver.getDriverNumber());
       dto.setCreationDate(driver.getCreationDate());

        Person person = driver.getPerson();
        if(person != null){
            dto.setPersonId(person.getId());
            dto.setPersonFullName(person.getFirstName() + " " + person.getLastName());
        }
        return dto;
    }
    List<DriverResponseDTO> toResponseDTOList(List<Driver> drivers);
}