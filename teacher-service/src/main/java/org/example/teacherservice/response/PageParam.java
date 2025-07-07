package org.example.teacherservice.response;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 分页查询参数
 */
@Data
public class PageParam {
    @Min(1)
    private Integer pageNum = 1;

    @Min(10)
    @Max(100)
    private Integer pageSize = 20;

    private String orderBy;
}
