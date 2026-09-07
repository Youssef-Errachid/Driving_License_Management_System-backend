package com.drivinglicense.dto.person;

import com.drivinglicense.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonCreateDTO {

    @NotBlank(message = "national number is required")
    private String nationalNumber;

    @NotBlank(message = "first name is required")
    private String firstName;

    @NotBlank(message = "last name is required")
    private String lastName;

    @NotNull(message = "birth day is required")
    @Past(message = "the birth day should be in the past")
    private LocalDate birthDay;

    private String address;

    @Pattern(regexp = "^[0-9+ ]{8,15}$", message = "invalid phone number")
    private String phoneNumber;

    @Email(message = "invalid email")
    @NotBlank(message = "email is required")
    private String email;

    private String nationality;
    private String photo;
    private Gender gender;
}