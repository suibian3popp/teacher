package org.example.teacherservice.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("assignment_submissions")
public class AssignmentSubmission {
    @TableId(value = "submission_id", type = IdType.AUTO)
    private Integer submissionId;

    @TableField("assignment_class_id")
    private Integer assignmentClassId;

    @TableField("student_id")
    private Integer studentId;

    @TableField(value = "submit_time", fill = FieldFill.INSERT)
    private Date submitTime;

    @TableField("resource_id")
    private Integer resourceId;
}
