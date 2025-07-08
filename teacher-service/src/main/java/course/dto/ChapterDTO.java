package course.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ChapterDTO {

    @JsonProperty("chapter_id")
    private Integer chapterId;

    @JsonProperty("course_id")
    private Integer courseId;

    @JsonProperty("parent_id")
    private Integer parentId;

    @JsonProperty("level")
    private Integer level;

    @JsonProperty("title")
    private String title;

    @JsonProperty("order_num")
    private Integer orderNum;

    @JsonProperty("resources")
    private List<ResourceDTO> resources;

    @JsonProperty("children")
    private List<ChapterDTO> children;
} 