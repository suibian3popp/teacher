package org.example.teacherservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CourseCreateDTO {
    @JsonProperty("name")
    private String name;

    @JsonProperty("teacher_id")
    private Integer teacherId;

    @JsonProperty("assistant_id")
    private Integer assistantId;

    @JsonProperty("department_id")
    private Integer departmentId;

    @JsonProperty("semester")
    private String semester;

    @JsonProperty("description")
    private String description;

    @JsonProperty("course_type")
    private String courseType;

    @JsonProperty("cover_image_resource")
    private Integer coverImageResource;
}
