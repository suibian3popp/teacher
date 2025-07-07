package org.example.teacherservice.dto;

import lombok.Data;

@Data
public class ResourcesQueryDTO
{
    //筛选条件
    private String difficulty;
    private String type;
    private String permission;
    private String fileSize;
    private String sortBy;//排序字段
    private String sortDir;//排序方向（asc/desc）

    //分页参数
    private Integer page;//当前页码（从1开始）
    private Integer pageSize;//每页条数

    //搜索关键词
    private String keyword;
}