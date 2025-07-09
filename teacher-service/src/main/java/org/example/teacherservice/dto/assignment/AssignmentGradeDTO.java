package org.example.teacherservice.dto.assignment;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 作业批改请求参数
 */
@Data
public class AssignmentGradeDTO {
    @NotNull(message = "提交记录ID不能为空,学生的作业资源ID")
    private Integer submissionId;

    private Integer graderId;

    @DecimalMin(value = "0.00", message = "分数不能为负数")
    private BigDecimal score;

    @Size(max = 500, message = "评语长度不能超过500字符")
    private String feedback;
}
