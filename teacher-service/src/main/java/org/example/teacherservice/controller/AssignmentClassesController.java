package org.example.teacherservice.controller;

import org.example.teacherservice.service.AssignmentClassesService;
import org.example.teacherservice.vo.ClassSimpleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/service/assignmentClasses")
public class AssignmentClassesController {

    @Autowired
    private AssignmentClassesService assignmentClassesService;


    /**
     * 批量添加作业-班级关联
     * POST /service/assignmentClasses/batch
     */
    @PostMapping("/batch")
    public ResponseEntity<Void> batchAddClassesToAssignment(
            @RequestParam Integer assignmentId,
            @RequestBody List<Integer> classIds) {
        System.out.println("给多个班级都安排这个作业");
        System.out.println("作业ID"+assignmentId);
        System.out.println("班级列表：");
        for (Integer classId : classIds) {
            System.out.println("班级ID"+classId);
        }
        assignmentClassesService.batchAddClassesToAssignment(assignmentId, classIds);
        return ResponseEntity.ok().build();
    }

    /**
     * 移除单个作业-班级关联
     * DELETE /service/assignmentClasses
     */
    @DeleteMapping
    public ResponseEntity<Void> removeClassFromAssignment(
            @RequestParam Integer assignmentId,
            @RequestParam Integer classId) {
        System.out.println("删除这个班级的这个作业");
        System.out.println("作业ID"+assignmentId);
        System.out.println("班级ID"+classId);
        assignmentClassesService.removeClassFromAssignment(assignmentId, classId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 清除作业的所有班级关联
     * DELETE /service/assignmentClasses/{assignmentId}/all
     */
    @DeleteMapping("/{assignmentId}/all")
    public ResponseEntity<Void> clearAssignmentClasses(
            @PathVariable Integer assignmentId) {
        System.out.println("清除作业的所有班级关联，在删除这个作业之前要做的");
        System.out.println("删除作业ID"+assignmentId);
        assignmentClassesService.clearAssignmentClasses(assignmentId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 获取作业关联的班级列表
     * GET /service/assignmentClasses/{assignmentId}/classes
     */
    @GetMapping("/{assignmentId}/classes")
    public ResponseEntity<List<ClassSimpleVO>> getClassesByAssignment(
            @PathVariable Integer assignmentId) {
        System.out.println("查看这个作业布置给了哪些班级");
        System.out.println("assignmentId"+assignmentId);
        List<ClassSimpleVO> classes = assignmentClassesService.getClassesByAssignment(assignmentId);
        return ResponseEntity.ok(classes);
    }

    /**
     * 批量获取作业关联的班级名称
     * POST /service/assignmentClasses/classNames
     */
    @PostMapping("/classNames")
    public ResponseEntity<Map<Integer, List<String>>> getClassNamesByAssignments(
            @RequestBody List<Integer> assignmentIds) {
        System.out.println("批量获取作业关联的班级名称");
        System.out.println("作业ID"+assignmentIds);
        Map<Integer, List<String>> result = assignmentClassesService.getClassNamesByAssignments(assignmentIds);
        return ResponseEntity.ok(result);
    }
}