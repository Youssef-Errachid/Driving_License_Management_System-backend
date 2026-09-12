package com.drivinglicense.controller;

import com.drivinglicense.dto.auth.LoginRequestDTO;
import com.drivinglicense.dto.auth.LoginResponseDTO;
import com.drivinglicense.dto.common.ApiResponseDTO;
import com.drivinglicense.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> login(@Valid  @RequestBody LoginRequestDTO dto){
        LoginResponseDTO response = authService.login(dto);
        return ResponseEntity.ok(new ApiResponseDTO<>(true , "Login successful.", response));
    }

}