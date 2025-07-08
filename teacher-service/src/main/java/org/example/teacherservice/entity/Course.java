package org.example.teacherservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("courses")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @TableId(value = "course_id", type = IdType.AUTO)
    private Integer courseId;

    @TableField("name")
    private String name;

    @TableField("teacher_id")
    private Integer teacherId;

    @TableField("assistant_id")
    private Integer assistantId;

    @TableField("department_id")
    private Integer departmentId;

    @TableField("semester")
    private String semester;

    @TableField("description")
    private String description;

    @TableField("course_type")
    private String courseType;

    @TableField("cover_image_resource")
    private Integer coverImageResource;
}