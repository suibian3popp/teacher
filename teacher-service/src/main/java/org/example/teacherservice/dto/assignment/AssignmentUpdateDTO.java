package org.example.teacherservice.dto.assignment;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 作业更新请求参数
 * 教师
 */
@Data
public class AssignmentUpdateDTO {
    @NotNull(message = "作业ID不能为空")
    private Integer assignmentId;

    private String title;

    private String description;

    private Integer resourceId;

    @DecimalMin(value = "0.01", message = "总分必须大于0")
    private BigDecimal totalScore;
}
