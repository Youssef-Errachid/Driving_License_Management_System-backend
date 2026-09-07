package com.drivinglicense.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false,updatable = false)
    private String driverNumber;

    @Builder.Default
    private LocalDate creationDate = LocalDate.now();

    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id", unique = true ,nullable = false)
    @NotNull(message = "the user should be related to an existing person")
    private Person person;
}
