package org.example.teacherservice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("exam_classes")
public class ExamClasses {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("exam_id")
    private Integer examId;

    @TableField("class_id")
    private Integer classId;
}