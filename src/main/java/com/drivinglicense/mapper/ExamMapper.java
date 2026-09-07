package com.drivinglicense.mapper;

import com.drivinglicense.dto.exam.ExamResponseDTO;
import com.drivinglicense.dto.exam.ExamResultDTO;
import com.drivinglicense.dto.exam.ExamScheduleDTO;
import com.drivinglicense.entity.Exam;
import com.drivinglicense.entity.PracticalExam;
import com.drivinglicense.entity.TheoryExam;
import com.drivinglicense.entity.VisionExam;
import com.drivinglicense.enums.ExamType;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExamMapper {
    default Exam toEntity(ExamScheduleDTO dto){
        Exam exam;

        if(dto.getExamType() == ExamType.VISION){
            exam = new VisionExam();
        }
        else if (dto.getExamType() == ExamType.THEORY){
            exam = new TheoryExam();
        }
        else if (dto.getExamType() == ExamType.PRACTICAL){
            exam = new PracticalExam();
        }
        else{
            throw new IllegalArgumentException("Unknown exam type: " + dto.getExamType());
        }
        exam.setAppointmentDate(dto.getAppointmentDate());
        return exam;
    }

    default ExamResponseDTO toResponseDTO(Exam exam){
        ExamResponseDTO dto = new ExamResponseDTO();
        dto.setId(exam.getId());
        dto.setAppointmentDate(exam.getAppointmentDate());
        dto.setResultDate(exam.getResultDate());
        dto.setExamResult(exam.getResult());

        if(exam.getRequest() != null){
            dto.setRequestId(exam.getRequest().getId());
        }

        if(exam instanceof VisionExam){
            dto.setExamType(ExamType.VISION);
        }
        else if (exam instanceof TheoryExam theoryExam){
            dto.setExamType(ExamType.THEORY);
            dto.setScore(theoryExam.getScore());
        }
        else if(exam instanceof PracticalExam){
            dto.setExamType(ExamType.PRACTICAL);
        }

        return dto;
    }

    default void updateResultFromDTO(ExamResultDTO dto, Exam exam){
        exam.setResult(dto.getResult());
        exam.setResultDate(java.time.LocalDate.now());

        if(exam instanceof TheoryExam theoryExam){
            theoryExam.setScore(dto.getScore());
        }
    }

    List<ExamResponseDTO> toResponseDTOList(List<Exam> exams);
}