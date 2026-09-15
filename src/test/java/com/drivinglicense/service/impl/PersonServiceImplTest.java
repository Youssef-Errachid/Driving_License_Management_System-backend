package com.drivinglicense.service.impl;

import com.drivinglicense.dto.common.PageResponseDTO;
import com.drivinglicense.dto.person.PersonCreateDTO;
import com.drivinglicense.dto.person.PersonResponseDTO;
import com.drivinglicense.dto.person.PersonUpdateDTO;
import com.drivinglicense.entity.Person;
import com.drivinglicense.enums.Gender;
import com.drivinglicense.exception.BusinessException;
import com.drivinglicense.exception.ResourceNotFoundException;
import com.drivinglicense.repository.PersonRepository;
import com.drivinglicense.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PersonServiceImplTest {

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    private PersonCreateDTO buildCreateDTO(String nationalNumber) {
        PersonCreateDTO dto = new PersonCreateDTO();
        dto.setNationalNumber(nationalNumber);
        dto.setFirstName("Youssef");
        dto.setLastName("Errachid");
        dto.setBirthDay(LocalDate.of(2000, 1, 1));
        dto.setEmail(nationalNumber.toLowerCase() + "@example.com");
        dto.setGender(Gender.MALE);
        return dto;
    }

    @Test
    void create_shouldPersistPerson_andReturnResponseDTO() {
        PersonCreateDTO dto = buildCreateDTO("H2TEST001");

        PersonResponseDTO result = personService.create(dto);

        assertThat(result.getId()).isNotNull();
        assertThat(personRepository.existsByNationalNumber("H2TEST001")).isTrue();
        assertThat(result.getFirstName()).isEqualTo("Youssef");
    }

    @Test
    void create_shouldThrowBusinessException_whenNationalNumberAlreadyExists() {
        Person existing = Person.builder()
                .nationalNumber("H2TEST002")
                .firstName("Ali")
                .lastName("Bennani")
                .birthDay(LocalDate.of(1995, 5, 5))
                .email("ali@example.com")
                .build();
        personRepository.save(existing);

        PersonCreateDTO dto = buildCreateDTO("H2TEST002");

        assertThatThrownBy(() -> personService.create(dto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("national number already exists");
    }

    @Test
    void getById_shouldThrowResourceNotFoundException_whenNotExists() {
        assertThatThrownBy(() -> personService.getById(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getAll_shouldReturnPagedPersons() {
        personRepository.save(Person.builder()
                .nationalNumber("H2TEST003")
                .firstName("Sara")
                .lastName("Amrani")
                .birthDay(LocalDate.of(1998, 3, 3))
                .email("sara@example.com")
                .build());

        PageResponseDTO<PersonResponseDTO> page = personService.getAll(0, 10);

        assertThat(page.getContent()).isNotEmpty();
        assertThat(page.getTotalElements()).isGreaterThanOrEqualTo(1);
    }

    @Test
    void search_shouldFindByPartialFirstName() {
        personRepository.save(Person.builder()
                .nationalNumber("H2TEST004")
                .firstName("Mehdi")
                .lastName("Tazi")
                .birthDay(LocalDate.of(1992, 7, 7))
                .email("mehdi@example.com")
                .build());

        var results = personService.search("Mehdi");

        assertThat(results).extracting("firstName").contains("Mehdi");
    }

    @Test
    void update_shouldModifyOnlyProvidedFields() {
        Person saved = personRepository.save(Person.builder()
                .nationalNumber("H2TEST005")
                .firstName("Omar")
                .lastName("Idrissi")
                .birthDay(LocalDate.of(1990, 1, 1))
                .email("omar@example.com")
                .build());

        PersonUpdateDTO updateDTO = new PersonUpdateDTO();
        updateDTO.setFirstName("OmarUpdated");

        PersonResponseDTO result = personService.update(saved.getId(), updateDTO);

        assertThat(result.getFirstName()).isEqualTo("OmarUpdated");
        assertThat(result.getLastName()).isEqualTo("Idrissi");
    }
}