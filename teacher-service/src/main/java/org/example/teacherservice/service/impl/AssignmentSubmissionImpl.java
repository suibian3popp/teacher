package org.example.teacherservice.service.impl;

import org.example.teacherservice.dto.assignment.AssignmentSubmitDTO;
import org.example.teacherservice.service.AssignmentSubmissionService;
import org.example.teacherservice.vo.SubmissionVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AssignmentSubmissionImpl implements AssignmentSubmissionService {
    @Override
    public void submitAssignment(AssignmentSubmitDTO dto) {

    }

    @Override
    public SubmissionVO getStudentSubmission(Integer assignmentId, Integer studentId) {
        return null;
    }

    @Override
    public List<SubmissionVO> listStudentSubmissions(Integer studentId) {
        return List.of();
    }

    @Override
    public Map<Integer, Integer> countSubmissionsByAssignments(List<Integer> assignmentIds) {
        return Map.of();
    }

    @Override
    public boolean hasStudentSubmitted(Integer assignmentId, Integer studentId) {
        return false;
    }
}
