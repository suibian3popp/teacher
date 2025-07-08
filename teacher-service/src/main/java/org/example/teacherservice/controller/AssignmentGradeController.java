package org.example.teacherservice.controller;

import org.example.teachercommon.entity.JwtUserInfo;
import org.example.teacherservice.dto.assignment.AssignmentGradeDTO;
import org.example.teacherservice.service.AssignmentGradeService;
import org.example.teacherservice.util.UserContext;
import org.example.teacherservice.vo.GradeDetailVO;
import org.example.teacherservice.vo.GradingProgressVO;
import org.example.teacherservice.vo.assignment.GradeStatsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/service/assignmentGrade")
public class AssignmentGradeController {

    @Autowired
    private AssignmentGradeService gradeService;


    /**
     * 批改或更新作业成绩
     * POST /service/assignmentGrade/grade
     */
    @PostMapping("/grade")
    public ResponseEntity<Void> gradeAssignment(@RequestBody AssignmentGradeDTO dto) {
        System.out.println("批改或更新作业成绩");
        System.out.println(dto.getClass());
        JwtUserInfo currentUser = UserContext.get();
        dto.setGraderId(currentUser.getUserId());
        System.out.println("批改人ID"+dto.getGraderId());
        System.out.println("评分："+dto.getScore());
        gradeService.gradeAssignment(dto);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除批改记录
     * DELETE /service/assignmentGrade/{gradeId}
     */
    @DeleteMapping("/{gradeId}")
    public ResponseEntity<Void> deleteGrade(@PathVariable Integer gradeId) {
        System.out.println("删除批改记录");
        System.out.println("记录ID："+gradeId);
        gradeService.deleteGrade(gradeId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 获取作业成绩统计概览
     * GET /service/assignmentGrade/stats?assignmentClassId={}
     */
    @GetMapping("/stats")
    public ResponseEntity<GradeStatsVO> getAssignmentStats(
            @RequestParam Integer assignmentClassId) {
        System.out.println("获取作业成绩统计概览");
        System.out.println("当前作业ID"+assignmentClassId);
        GradeStatsVO stats = gradeService.getAssignmentStats(assignmentClassId);
        return ResponseEntity.ok(stats);
    }

    /**
     * 获取班级批改进度
     * GET /service/assignmentGrade/progress?assignmentClassId={}
     */
    @GetMapping("/progress")
    public ResponseEntity<GradingProgressVO> getGradingProgress(
            @RequestParam Integer assignmentClassId) {
        System.out.println("获取当前作业，班级批改进度");
        System.out.println("当前作业和班级的关联ID"+assignmentClassId);
        GradingProgressVO progress = gradeService.getGradingProgress(assignmentClassId);
        return ResponseEntity.ok(progress);
    }

    /**
     * 获取作业的所有批改记录
     * GET /service/assignmentGrade/all?assignmentClassId={}
     */
    @GetMapping("/all")
    public ResponseEntity<List<GradeDetailVO>> listAssignmentGrades(
            @RequestParam Integer assignmentClassId) {
        System.out.println("获取作业的所有批改记录");
        System.out.println("当前作业和班级的关联ID"+assignmentClassId);
        List<GradeDetailVO> grades = gradeService.listAssignmentGrades(assignmentClassId);
        return ResponseEntity.ok(grades);
    }

    /**
     * 批量获取作业的已批改数量
     * POST /service/assignmentGrade/count
     */
    @PostMapping("/count")
    public ResponseEntity<Map<Long, Integer>> countGradedAssignments(
            @RequestBody List<Integer> assignmentClassIds) {
        System.out.println("批量获取作业的已批改数量");
        System.out.println("班级作业关联ID列表");
        for (Integer assignmentClassId : assignmentClassIds) {
            System.out.println("作业ID"+assignmentClassId);
        }
        Map<Long, Integer> counts = gradeService.countGradedAssignments(assignmentClassIds);
        return ResponseEntity.ok(counts);
    }

    /**
     * 获取学生某次作业的成绩
     * GET /service/assignmentGrade/student?assignmentClassId={}&studentId={}
     */
    @GetMapping("/student")
    public ResponseEntity<GradeDetailVO> getStudentGrade(
            @RequestParam Integer assignmentClassId,
            @RequestParam Integer studentId) {
        System.out.println("获取学生某次作业的成绩");
        System.out.println("作业关联班级ID"+assignmentClassId);
        System.out.println("学生ID"+studentId);
        GradeDetailVO grade = gradeService.getStudentGrade(assignmentClassId, studentId);
        return ResponseEntity.ok(grade);
    }

    /**
     * 批量更新作业成绩
     * PUT /service/assignmentGrade/batch
     */
    @PutMapping("/batch")
    public ResponseEntity<Integer> batchUpdateGrades(
            @RequestBody List<AssignmentGradeDTO> gradeDTOs) {
        System.out.println("批量更新作业");
        for (AssignmentGradeDTO gradeDTO : gradeDTOs) {
            JwtUserInfo currentUser = UserContext.get();
            gradeDTO.setGraderId(currentUser.getUserId());
            System.out.println("批改者ID"+gradeDTO.getGraderId());
            System.out.println("分数"+gradeDTO.getScore());
        }
        int updatedCount = gradeService.batchUpdateGrades(gradeDTOs);
        return ResponseEntity.ok(updatedCount);
    }
}