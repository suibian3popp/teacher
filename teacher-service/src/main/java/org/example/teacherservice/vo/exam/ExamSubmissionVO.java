package org.example.teacherservice.vo.exam;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 考试提交信息视图对象
 */
@Data
public class ExamSubmissionVO {
    private Integer submissionId;
    private Integer examClassId;
    private Integer studentId;
    private String studentName; // 需要关联students表
    private String studentNumber; // 学号
    private LocalDateTime submitTime;
    private Integer resourceId;
    private String answerSheetUrl; // 需要关联resources表
    private ExamGradeVO gradeInfo; // 批改信息
}