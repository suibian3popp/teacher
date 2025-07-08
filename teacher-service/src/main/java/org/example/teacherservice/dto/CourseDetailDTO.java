package org.example.teacherservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CourseDetailDTO {

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

    @JsonProperty("chapters")
    private List<ChapterDTO> chapters;
}
