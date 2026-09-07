package com.drivinglicense.dto.person;

import com.drivinglicense.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonUpdateDTO {

    private String firstName;
    private String lastName;
    private String address;

    @Pattern(regexp = "^[0-9+ ]{8,15}$", message = "invalid phone number")
    private String phoneNumber;

    @Email(message = "invalid email")
    private String email;
    private String nationality;
    private String photo;
    private Gender gender;
}