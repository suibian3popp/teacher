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

    @NotBlank(message = "作业标题不能为空")
    private String title;

    private String description;

    @NotNull(message = "截止时间不能为空")
    @Future(message = "截止时间必须是将来的时间")
    private Date deadline;

    @NotNull(message = "总分不能为空")
    @DecimalMin(value = "0.01", message = "总分必须大于0")
    private BigDecimal totalScore;
}
