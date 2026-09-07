package com.drivinglicense.dto.user;

import com.drivinglicense.enums.Role;
import com.drivinglicense.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
    private Role role;
    private UserStatus userStatus;
}