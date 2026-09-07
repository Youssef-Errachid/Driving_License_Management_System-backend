package com.drivinglicense.dto.user;


import com.drivinglicense.enums.Role;
import com.drivinglicense.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String email;
    private Role role;
    private UserStatus userStatus;
    private LocalDate creationDate;
    private Long personId;
    private String personFullName;
}