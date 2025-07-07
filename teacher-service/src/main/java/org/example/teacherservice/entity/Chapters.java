package org.example.teacherservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 课程章节表
 * @TableName chapters
 */
@TableName(value ="chapters")
@Data
public class Chapters {
    /**
     * 章节ID
     */
    @TableId(value = "chapter_id", type = IdType.AUTO)
    private Integer chapterId;

    /**
     * 所属课程ID
     */
    @TableField(value = "course_id")
    private Integer courseId;

    /**
     * 章节标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 父章节ID
     */
    @TableField(value = "parent_id")
    private Integer parentId;

    /**
     * 排序序号
     */
    @TableField(value = "order_num")
    private Integer orderNum;
}