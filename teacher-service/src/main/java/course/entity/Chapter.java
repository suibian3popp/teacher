package course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("chapters")
public class Chapter {

    @TableId(value = "chapter_id", type = IdType.AUTO)
    private Integer chapterId;

    @TableField("course_id")
    private Integer courseId;

    @TableField("title")
    private String title;

    @TableField("parent_id")
    private Integer parentId;

    @TableField("order_num")
    private Integer orderNum;
} 