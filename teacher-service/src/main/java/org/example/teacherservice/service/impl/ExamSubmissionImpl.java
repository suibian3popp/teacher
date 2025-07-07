package org.example.teacherservice.service.impl;

import org.example.teacherservice.entity.ExamSubmission;
import org.example.teacherservice.service.ExamSubmissionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamSubmissionImpl implements ExamSubmissionService {
    @Override
    public void submitExam(ExamSubmission submission) {

    }

    @Override
    public ExamSubmission getStudentSubmission(Integer examId, Integer studentId) {
        return null;
    }

    @Override
    public List<ExamSubmission> listExamSubmissions(Integer examId) {
        return List.of();
    }

    @Override
    public List<ExamSubmission> listStudentSubmissions(Integer studentId) {
        return List.of();
    }
}
