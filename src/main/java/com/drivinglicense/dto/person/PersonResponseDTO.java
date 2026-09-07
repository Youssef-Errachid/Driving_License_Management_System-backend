package com.drivinglicense.dto.person;

import com.drivinglicense.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponseDTO {

    private Long id;
    private String nationalNumber;
    private String firstName;
    private String lastName;
    private LocalDate birthDay;
    private String address;
    private String phoneNumber;
    private String email;
    private String nationality;
    private String photo;
    private Gender gender;
}