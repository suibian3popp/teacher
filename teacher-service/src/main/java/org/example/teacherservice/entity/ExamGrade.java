package org.example.teacherservice.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("exam_grades")
public class ExamGrade {
    @TableId(value = "grade_id", type = IdType.AUTO)
    private Integer gradeId;

    @TableField("submission_id")
    private Integer submissionId;

    @TableField("grader_id")
    private Integer graderId;

    @TableField("score")
    private BigDecimal score;

    @TableField("feedback")
    private String feedback;

    @TableField(value = "grade_time", fill = FieldFill.INSERT)
    private Date gradeTime;
}
