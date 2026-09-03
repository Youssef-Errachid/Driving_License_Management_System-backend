package com.drivinglicense.entity;

import com.drivinglicense.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "national number is required")
    @Column(unique = true, nullable = false)
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
    @Column(unique = true, nullable = false)
    private String email;

    private String nationality;

    private String photo;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToOne(mappedBy = "person")
    private User user;

    @OneToOne(mappedBy = "person")
    private Driver driver;

}
