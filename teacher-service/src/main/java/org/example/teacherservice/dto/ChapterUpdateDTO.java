package org.example.teacherservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChapterUpdateDTO {

    @JsonProperty("chapter_name")
    private String chapterName;
}
