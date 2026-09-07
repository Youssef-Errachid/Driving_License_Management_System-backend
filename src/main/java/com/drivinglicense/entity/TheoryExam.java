package com.drivinglicense.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@DiscriminatorValue("THEORY")
public class TheoryExam extends Exam{
    @Min(value = 0,message = "score should be positive not negative")
    @Max(value = 40 , message = "score can't exceed 40")
    private Integer score;

}
