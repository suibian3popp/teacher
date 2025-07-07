package org.example.teacherservice.dto.assignment;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 作业创建请求参数
 * 教师
 */
@Data
public class AssignmentCreateDTO {
    @NotBlank(message = "作业标题不能为空")
    private String title;

    private String description;

    @NotNull(message = "截止时间不能为空")
    @Future(message = "截止时间必须是将来的时间")
    private Date deadline;

    @NotNull(message = "总分不能为空")
    @DecimalMin(value = "0.01", message = "总分必须大于0")
    private BigDecimal totalScore;

    @NotNull(message = "关联资源ID不能为空")
    private Integer resourceId;

    @NotEmpty(message = "至少关联一个班级")
    private List<Integer> classIds; // 关联的班级ID列表
}
