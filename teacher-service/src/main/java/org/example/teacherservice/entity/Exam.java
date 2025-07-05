package org.example.teacherservice.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("exams")
public class Exam {
    @TableId(value = "exam_id", type = IdType.AUTO)
    private Integer examId;

    @TableField("resource_id")
    private Integer resourceId;

    @TableField("total_score")
    private BigDecimal totalScore;

    @TableField("time_limit")
    private Integer timeLimit;

    @TableField("start_time")
    private Date startTime;

    @TableField("end_time")
    private Date endTime;
}
