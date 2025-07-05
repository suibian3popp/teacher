package org.demo_hd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 课程信息表
 * @TableName courses
 */
@TableName(value ="courses")
@Data
public class Courses {
    /**
     * 课程ID
     */
    @TableId(value = "course_id", type = IdType.AUTO)
    private Integer courseId;

    /**
     * 课程名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 主讲教师ID
     */
    @TableField(value = "teacher_id")
    private Integer teacherId;

    /**
     * 所属院系ID
     */
    @TableField(value = "department_id")
    private Integer departmentId;

    /**
     * 开设学期
     */
    @TableField(value = "semester")
    private String semester;

    /**
     * 课程简介
     */
    @TableField(value = "description")
    private String description;

    /**
     * 没有混合课，只有直播和录播
     */
    @TableField(value = "course_type")
    private Object courseType;

    /**
     * 封面图资源ID
     */
    @TableField(value = "cover_image_resource")
    private Integer coverImageResource;
}