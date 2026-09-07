package com.drivinglicense.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Builder
@DiscriminatorValue("VISION")
public class VisionExam extends Exam{
}
