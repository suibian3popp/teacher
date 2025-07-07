package org.example.teacherservice.vo;

import lombok.Data;

import java.util.List;

/**
 * 作业详情视图对象
 */
@Data
public class AssignmentDetailVO extends AssignmentVO {
    private String description;
    private Integer resourceId;
    private List<ClassSimpleVO> classes; // 关联班级详细信息
}