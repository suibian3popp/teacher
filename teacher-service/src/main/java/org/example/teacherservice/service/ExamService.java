package org.example.teacherservice.service;


import org.example.teacherservice.entity.Exam;
import org.example.teacherservice.entity.ExamClasses;

import java.util.List;

/**
 * 试卷管理服务
 * 负责试卷的创建、发布、删除及关联班级管理
 */
public interface ExamService {

    /**
     * 创建并发布试卷
     * @param exam 试卷基本信息
     * @param classIds 关联的班级ID列表
     * @return 发布成功的试卷ID
     */
    Integer publishExam(Exam exam, List<Integer> classIds);

    /**
     * 删除试卷（级联删除关联记录）
     * @param examId 试卷ID
     */
    void deleteExam(Integer examId);

    /**
     * 更新试卷信息
     * @param exam 试卷更新数据
     */
    void updateExam(Exam exam);

    /**
     * 获取教师发布的试卷列表
     * @param teacherId 教师ID
     * @return 试卷列表
     */
    List<Exam> listExamsByTeacher(Integer teacherId);

    /**
     * 获取试卷详情
     * @param examId 试卷ID
     * @return 试卷详情
     */
    Exam getExamDetail(Integer examId);

    /**
     * 关联班级到试卷
     * @param examClasses 试卷班级关联信息
     */
    void addClassToExam(ExamClasses examClasses);
}