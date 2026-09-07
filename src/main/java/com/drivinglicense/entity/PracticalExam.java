package com.drivinglicense.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Builder
@DiscriminatorValue("PRACTICAL")
public class PracticalExam extends Exam{
}
