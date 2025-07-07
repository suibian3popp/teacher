package org.example.teacherservice.service;

import org.example.teacherservice.entity.ExamSubmission;

import java.util.List;

/**
 * 试卷提交服务
 * 处理学生试卷提交及相关查询
 */
public interface ExamSubmissionService {

    /**
     * 提交试卷
     * @param submission 试卷提交信息
     * @throws IllegalStateException 如果超过截止时间
     */
    void submitExam(ExamSubmission submission);

    /**
     * 获取学生的试卷提交记录
     * @param examId 试卷ID
     * @param studentId 学生ID
     * @return 提交记录
     */
    ExamSubmission getStudentSubmission(Integer examId, Integer studentId);

    /**
     * 获取某次考试的所有提交记录
     * @param examId 试卷ID
     * @return 提交记录列表
     */
    List<ExamSubmission> listExamSubmissions(Integer examId);

    /**
     * 获取学生所有已提交的试卷
     * @param studentId 学生ID
     * @return 提交记录列表
     */
    List<ExamSubmission> listStudentSubmissions(Integer studentId);
}
