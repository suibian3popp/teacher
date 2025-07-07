package org.example.teacherservice.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 作业列表视图对象
 */
@Data
public class AssignmentVO {
    private Integer assignmentId;
    private String title;
    private Date deadline;
    private BigDecimal totalScore;
    private Integer submittedCount; // 已提交人数
    private Integer gradedCount;    // 已批改人数
    private List<String> classNames; // 关联的班级名称列表
}
