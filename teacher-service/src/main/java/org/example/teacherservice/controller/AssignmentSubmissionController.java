package org.example.teacherservice.controller;

import org.example.teacherservice.dto.assignment.AssignmentSubmitDTO;
import org.example.teacherservice.service.AssignmentSubmissionService;
import org.example.teacherservice.vo.SubmissionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 作业提交管理控制器
 * 接口路径前缀：/service/assignmentSubmission
 */
@RestController
@RequestMapping("/service/assignmentSubmission")
public class AssignmentSubmissionController {

    @Autowired
    private AssignmentSubmissionService submissionService;


    /**
     * 提交作业
     * POST /service/assignmentSubmission/submit
     */
    @PostMapping("/submit")
    public ResponseEntity<Void> submitAssignment(@RequestBody AssignmentSubmitDTO dto) {
        System.out.println("学生提交作业"+dto.getStudentId());
        System.out.println("上传资源ID"+dto.getResourceId());
        System.out.println("作业和班级的关联ID"+dto.getAssignmentClassId());
        System.out.println("班级ID"+dto.getClass());
        submissionService.submitAssignment(dto);
        return ResponseEntity.ok().build();
    }

    /**
     * 获取学生单次作业提交记录
     * GET /service/assignmentSubmission/student/submission?assignmentClassId={}&studentId={}
     */
    @GetMapping("/student/submission")
    public ResponseEntity<SubmissionVO> getStudentSubmission(
            @RequestParam Integer assignmentClassId,
            @RequestParam Integer studentId) {
        System.out.println("获取学生单次作业提交记录");
        System.out.println("作业班级关联ID:"+assignmentClassId);
        System.out.println("学生ID:"+studentId);
        SubmissionVO submission = submissionService.getStudentSubmission(assignmentClassId, studentId);
        return ResponseEntity.ok(submission);
    }

    /**
     * 获取学生的所有提交记录
     * GET /service/assignmentSubmission/student/all?studentId={}
     */
    @GetMapping("/student/all")
    public ResponseEntity<List<SubmissionVO>> listStudentSubmissions(
            @RequestParam Integer studentId) {
        System.out.println("获取当前学生的所有提交记录");
        List<SubmissionVO> submissions = submissionService.listStudentSubmissions(studentId);
        return ResponseEntity.ok(submissions);
    }

    /**
     * 批量获取作业提交数量
     * POST /service/assignmentSubmission/count
     */
    @PostMapping("/count")
    public ResponseEntity<Map<Integer, Integer>> countSubmissions(
            @RequestBody List<Integer> assignmentClassIds) {
        System.out.println("批量获取作业提交数量");
        for (int assignmentClassId : assignmentClassIds) {
            System.out.println("作业和班级关联ID:"+assignmentClassId);
        }
        Map<Integer, Integer> counts = submissionService.countSubmissionsByAssignments(assignmentClassIds);
        return ResponseEntity.ok(counts);
    }

    /**
     * 检查学生是否已提交作业
     * GET /service/assignmentSubmission/check?assignmentClassId={}&studentId={}
     */
    @GetMapping("/check")
    public ResponseEntity<Boolean> hasStudentSubmitted(
            @RequestParam Integer assignmentClassId,
            @RequestParam Integer studentId) {
        System.out.println("检查该名学生是否提交作业");
        System.out.println("学生ID："+studentId);
        System.out.println("作业ID:"+assignmentClassId);
        boolean hasSubmitted = submissionService.hasStudentSubmitted(assignmentClassId, studentId);
        return ResponseEntity.ok(hasSubmitted);
    }

    /**
     * 获取作业的所有提交学生ID
     * GET /service/assignmentSubmission/students?assignmentClassId={}
     */
    @GetMapping("/students")
    public ResponseEntity<List<Integer>> listSubmittedStudents(
            @RequestParam Integer assignmentClassId) {
        System.out.println("获取作业的所有提交学生ID");
        System.out.println("班级作业关联ID:"+assignmentClassId);
        List<Integer> studentIds = submissionService.listSubmittedStudents(assignmentClassId);
        return ResponseEntity.ok(studentIds);
    }

    /**
     * 获取作业的所有提交记录
     * GET /service/assignmentSubmission/all?assignmentClassId={}
     */
    @GetMapping("/all")
    public ResponseEntity<List<SubmissionVO>> listSubmissionsByAssignment(
            @RequestParam Integer assignmentClassId) {
        System.out.println("获取当前班级该作业的所有提交记录");
        System.out.println("作业和班级关联ID："+assignmentClassId);
        List<SubmissionVO> submissions = submissionService.listSubmissionsByAssignment(assignmentClassId);
        return ResponseEntity.ok(submissions);
    }
}