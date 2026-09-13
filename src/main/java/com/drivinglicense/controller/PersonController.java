package com.drivinglicense.controller;

import com.drivinglicense.dto.common.ApiResponseDTO;
import com.drivinglicense.dto.common.PageResponseDTO;
import com.drivinglicense.dto.person.PersonCreateDTO;
import com.drivinglicense.dto.person.PersonResponseDTO;
import com.drivinglicense.dto.person.PersonUpdateDTO;
import com.drivinglicense.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @PreAuthorize("hasRole('AGENT')")
    @PostMapping
    public ResponseEntity<ApiResponseDTO<PersonResponseDTO>> create(@Valid @RequestBody PersonCreateDTO dto) {
        PersonResponseDTO response = personService.create(dto);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Person created successfully.", response));
    }

    @PreAuthorize("hasRole('AGENT')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<PersonResponseDTO>> getById(@PathVariable Long id) {
        PersonResponseDTO response = personService.getById(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Person retrieved successfully.", response));
    }

    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<PersonResponseDTO>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponseDTO<PersonResponseDTO> response = personService.getAll(page, size);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Persons retrieved successfully.", response));
    }

    @PreAuthorize("hasRole('AGENT')")
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDTO<List<PersonResponseDTO>>> search(@RequestParam String query) {
        List<PersonResponseDTO> response = personService.search(query);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Search results.", response));
    }

    @PreAuthorize("hasRole('AGENT')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<PersonResponseDTO>> update(
            @PathVariable Long id, @Valid @RequestBody PersonUpdateDTO dto) {
        PersonResponseDTO response = personService.update(id, dto);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Person updated successfully.", response));
    }
}