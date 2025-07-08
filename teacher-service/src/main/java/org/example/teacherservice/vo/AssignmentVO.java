package org.example.teacherservice.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 作业列表视图对象
 */

@Data
public class AssignmentVO {
    private Integer assignmentId;  //作业ID
    private String description;   //作业描述
    private Integer resourceId;   //资源ID
    private String title;     //作业标题
    private BigDecimal totalScore;   //总分
    private List<ClassStatisticsVO> classStatistics;
}

