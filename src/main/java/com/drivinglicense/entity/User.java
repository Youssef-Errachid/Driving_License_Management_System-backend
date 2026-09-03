package com.drivinglicense.entity;

import com.drivinglicense.enums.Role;
import com.drivinglicense.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "invalid email")
    @NotBlank(message = "email is required")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "password is required")
    private String password;

    @Builder.Default
    private LocalDate creationDate = LocalDate.now();

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id", unique = true)
    @NotNull(message = "the user should be related to an exist person")
    private Person person;
}
