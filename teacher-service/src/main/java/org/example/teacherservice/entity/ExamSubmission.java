package org.example.teacherservice.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("exam_submissions")
public class ExamSubmission {
    @TableId(value = "submission_id", type = IdType.AUTO)
    private Integer submissionId;

    @TableField("exam_id")
    private Integer examId;

    @TableField("student_id")
    private Integer studentId;

    @TableField(value = "submit_time", fill = FieldFill.INSERT)
    private Date submitTime;

    @TableField("resource_id")
    private Integer resourceId;
}
