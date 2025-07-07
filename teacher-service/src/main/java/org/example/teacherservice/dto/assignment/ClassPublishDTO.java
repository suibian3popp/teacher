package org.example.teacherservice.dto.assignment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

// 班级发布信息DTO
@Data
public class ClassPublishDTO {
    @NotNull
    private Integer classId;
    private LocalDateTime deadline;
}