package org.demo_hd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 章节资源关联表
 * @TableName chapter_resources
 */
@TableName(value ="chapter_resources")
@Data
public class ChapterResources {
    /**
     * 关联ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 章节ID
     */
    @TableField(value = "chapter_id")
    private Integer chapterId;

    /**
     * 资源ID
     */
    @TableField(value = "resource_id")
    private Integer resourceId;

    /**
     * 显示顺序
     */
    @TableField(value = "display_order")
    private Integer displayOrder;
}