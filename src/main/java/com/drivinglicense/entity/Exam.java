package com.drivinglicense.entity;

import com.drivinglicense.enums.ExamResult;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "exam_type")
public abstract class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ExamResult result;

    @NotNull(message = "appointment date is required")
    @Column(nullable = false)
    private LocalDate appointmentDate;

    private LocalDate resultDate;

    @NotNull(message = "the exam should be related with an existing request")
    @ManyToOne(optional = false)
    @JoinColumn(name = "request_id", nullable = false,updatable = false)
    private Request request;
}
