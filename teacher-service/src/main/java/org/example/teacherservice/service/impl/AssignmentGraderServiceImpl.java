package org.example.teacherservice.service.impl;

import org.example.teacherservice.dto.assignment.AssignmentGradeDTO;
import org.example.teacherservice.service.AssignmentGradeService;
import org.example.teacherservice.vo.GradeDetailVO;
import org.example.teacherservice.vo.GradeStatsVO;
import org.example.teacherservice.vo.GradingProgressVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AssignmentGraderServiceImpl implements AssignmentGradeService {
    @Override
    public void gradeAssignment(AssignmentGradeDTO dto) {

    }

    @Override
    public GradeStatsVO getAssignmentStats(Integer assignmentId) {
        return null;
    }

    @Override
    public GradingProgressVO getGradingProgress(Integer assignmentId, Integer classId) {
        return null;
    }

    @Override
    public List<GradeDetailVO> listAssignmentGrades(Integer assignmentId) {
        return null;
    }

    @Override
    public Map<Integer, Integer> countGradedAssignments(List<Integer> assignmentIds) {
        return null;
    }
}
