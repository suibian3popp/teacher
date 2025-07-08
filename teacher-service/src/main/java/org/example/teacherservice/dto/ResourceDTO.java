package org.example.teacherservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResourceDTO {

    @JsonProperty("resource_id")
    private Integer resourceId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("type")
    private String type;

    @JsonProperty("url")
    private String url;
}
