package org.example.teacherservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 教学资源表
 * @TableName resources
 */
@TableName(value ="resources")
@Data
public class Resources {
    @TableId(value = "resource_id", type = IdType.AUTO)
    private Integer resourceId;

    @TableField(value = "owner_id")
    private Integer ownerId;

    @TableField(value = "name")
    private String name;

    @TableField(value = "type")
    private Object type;

    @TableField(value = "permission")
    private Object permission;

    @TableField(value = "bucket")
    private String bucket;

    @TableField(value = "object_key")
    private String objectKey;

    @TableField(value = "file_size")
    private Long fileSize;

    @TableField(value = "upload_time")
    private Date uploadTime;

    @TableField(value = "difficulty")
    private Object difficulty;
}
