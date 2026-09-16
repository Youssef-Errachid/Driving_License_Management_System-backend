package com.drivinglicense.service;

import com.drivinglicense.dto.common.PageResponseDTO;
import com.drivinglicense.dto.user.UserCreateDTO;
import com.drivinglicense.dto.user.UserResponseDTO;
import com.drivinglicense.dto.user.UserUpdateDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO create(UserCreateDTO dto);

    UserResponseDTO getById(Long id);

    PageResponseDTO<UserResponseDTO> getAll(int page, int size);
    UserResponseDTO update(Long id, UserUpdateDTO dto);

    void delete(Long id);
}