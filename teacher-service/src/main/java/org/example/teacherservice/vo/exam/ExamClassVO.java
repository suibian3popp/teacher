package org.example.teacherservice.vo.exam;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 试卷关联班级信息视图对象
 */
@Data
public class ExamClassVO {
    private Integer id; // exam_classes表的id
    private Integer classId;
    private String className;
    private Integer courseId; // 来自classes表
    private String courseName; // 需要关联courses表
    private Integer studentCount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer publishStatus;
    private LocalDateTime publishTime;
    private Integer submissionCount; // 已提交人数
}
