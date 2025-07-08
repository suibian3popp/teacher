package course.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;

@Data
public class CourseDTO {

    @JsonProperty("course_id")
    private Integer courseId;

    @JsonProperty("course_name")
    private String courseName;

    @JsonProperty("course_type")
    private String courseType;

    @JsonProperty("teacher_id")
    private Integer teacherId;

    @JsonProperty("cover_image")
    private String coverImage;

    @JsonProperty("chapter_count")
    private Integer chapterCount;

    @JsonProperty("assistant_realname")
    private String assistantRealName;
} 