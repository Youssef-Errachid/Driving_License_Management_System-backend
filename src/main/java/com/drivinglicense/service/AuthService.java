package com.drivinglicense.service;

import com.drivinglicense.dto.auth.LoginRequestDTO;
import com.drivinglicense.dto.auth.LoginResponseDTO;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO dto);
}