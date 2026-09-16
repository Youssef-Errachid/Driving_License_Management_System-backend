package com.drivinglicense.controller;

import com.drivinglicense.dto.common.ApiResponseDTO;
import com.drivinglicense.dto.common.PageResponseDTO;
import com.drivinglicense.dto.user.UserCreateDTO;
import com.drivinglicense.dto.user.UserResponseDTO;
import com.drivinglicense.dto.user.UserUpdateDTO;
import com.drivinglicense.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> create(@Valid @RequestBody UserCreateDTO dto) {
        UserResponseDTO response = userService.create(dto);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "User created successfully.", response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getById(@PathVariable Long id) {
        UserResponseDTO response = userService.getById(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "User retrieved successfully.", response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<UserResponseDTO>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponseDTO<UserResponseDTO> response = userService.getAll(page, size);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Users retrieved successfully.", response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> update(
            @PathVariable Long id, @Valid @RequestBody UserUpdateDTO dto) {
        UserResponseDTO response = userService.update(id, dto);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "User updated successfully.", response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "User deleted successfully.", null));
    }
}