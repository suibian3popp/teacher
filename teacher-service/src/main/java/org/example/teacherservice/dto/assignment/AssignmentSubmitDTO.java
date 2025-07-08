package org.example.teacherservice.dto.assignment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 作业提交请求参数
 * 学生
 */
@Data
public class AssignmentSubmitDTO {
    @NotNull(message = "作业ID不能为空")
    private Integer assignmentClassId;

    @NotNull(message = "学生ID不能为空")
    private Integer studentId;

    @NotNull(message = "资源ID不能为空")
    private Integer resourceId; // 学生上传的文件资源ID
}
