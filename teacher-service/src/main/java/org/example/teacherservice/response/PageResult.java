package org.example.teacherservice.response;

import lombok.Data;

import java.util.List;

/**
 * 通用分页结果封装
 */
@Data
public class PageResult<T> {
    private Long total;
    private Integer pageSize;
    private Integer pageNum;
    private List<T> list;
}
