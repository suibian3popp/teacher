package course.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("chapter_resources")
public class ChapterResource {

    @TableField("chapter_id")
    private Integer chapterId;

    @TableField("resource_id")
    private Integer resourceId;
    
    @TableField("display_order")
    private Integer displayOrder;
} 