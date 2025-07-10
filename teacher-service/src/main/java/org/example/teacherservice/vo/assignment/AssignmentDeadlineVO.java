package org.example.teacherservice.vo.assignment;

import lombok.Data;
import java.util.Date;

/**
 * 作业截止时间视图对象
 * - 包含作业在各班级的截止时间信息
 */
@Data
public class AssignmentDeadlineVO {
    /**
     * 班级ID
     */
    private Integer classId;
    
    /**
     * 班级名称
     */
    private String className;
    
    /**
     * 作业截止时间
     */
    private Date deadline;
    
    /**
     * 作业发布时间
     */
    private Date publishTime;
    
    /**
     * 发布状态(0:未发布 1:已发布)
     */
    private Integer publishStatus;
} 