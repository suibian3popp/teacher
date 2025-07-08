package org.example.teacherservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChapterCreateDTO {

    @JsonProperty("course_id")
    private Integer courseId;

    @JsonProperty("parent_id")
    private Integer parentId;

    @JsonProperty("title")
    private String title;
}