package course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("resources")
public class Resource {

    @TableId(value = "resource_id", type = IdType.AUTO)
    private Integer resourceId;

    @TableField("owner_id")
    private Integer ownerId;

    @TableField("name")
    private String name;

    @TableField("type")
    private String type;

    @TableField("permission")
    private String permission;

    @TableField("bucket")
    private String bucket;

    @TableField("object_key")
    private String objectKey;

    @TableField("file_size")
    private Long fileSize;

    @TableField("upload_time")
    private LocalDateTime uploadTime;
} 