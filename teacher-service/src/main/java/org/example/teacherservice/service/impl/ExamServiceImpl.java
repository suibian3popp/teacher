package org.example.teacherservice.service.impl;

import org.example.teacherservice.entity.Exam;
import org.example.teacherservice.entity.ExamClasses;
import org.example.teacherservice.service.ExamService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {
    @Override
    public Integer publishExam(Exam exam, List<Integer> classIds) {
        return 0;
    }

    @Override
    public void deleteExam(Integer examId) {

    }

    @Override
    public void updateExam(Exam exam) {

    }

    @Override
    public List<Exam> listExamsByTeacher(Integer teacherId) {
        return List.of();
    }

    @Override
    public Exam getExamDetail(Integer examId) {
        return null;
    }

    @Override
    public void addClassToExam(ExamClasses examClasses) {

    }
}
