package com.drivinglicense.service;

import com.drivinglicense.dto.common.PageResponseDTO;
import com.drivinglicense.dto.person.PersonCreateDTO;
import com.drivinglicense.dto.person.PersonResponseDTO;
import com.drivinglicense.dto.person.PersonUpdateDTO;

import java.util.List;

public interface PersonService {

    PersonResponseDTO create(PersonCreateDTO dto);

    PersonResponseDTO getById(Long id);

    PageResponseDTO<PersonResponseDTO> getAll(int page, int size);

    List<PersonResponseDTO> search(String query);

    PersonResponseDTO update(Long id, PersonUpdateDTO dto);

}