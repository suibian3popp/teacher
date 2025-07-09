package org.example.teacherservice.controller;

import org.example.teacherservice.service.ClassStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service/classStudent")
public class ClassStudentController {

    @Autowired
    private ClassStudentService classStudentService;


    /**
     * 批量添加学生到班级
     * POST /service/classStudent/batchAdd
     */
    @PostMapping("/batchAdd")
    public ResponseEntity<Integer> batchAddStudentsToClass(
            @RequestParam Integer classId,
            @RequestParam Integer courseId,
            @RequestBody List<Integer> studentIds) {
        System.out.println("给班级批量添加学生");
        System.out.println("班级ID:"+classId);
        System.out.println("课程ID:"+courseId);
        System.out.println("学生ID列表：");
        for (Integer studentId : studentIds) {
            System.out.println("studentId:"+studentId);
        }
        int successCount=0;
        try{
            successCount = classStudentService.batchAddStudentsToClass(classId, studentIds, courseId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(successCount);
    }

    /**
     * 从班级中移除单个学生
     * DELETE /service/classStudent/remove
     */
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeStudentFromClass(
            @RequestParam Integer classId,
            @RequestParam Integer studentId) {
        System.out.println("从班级中移除单个学生");
        System.out.println("班级ID:"+classId);
        System.out.println("学生ID:"+studentId);
        classStudentService.removeStudentFromClass(classId, studentId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 批量从班级中移除学生
     * DELETE /service/classStudent/batchRemove
     */
    @DeleteMapping("/batchRemove")
    public ResponseEntity<Integer> batchRemoveStudentsFromClass(
            @RequestParam Integer classId,
            @RequestBody List<Integer> studentIds) {
        System.out.println("从班级中批量移除学生");
        System.out.println("班级列表ID:"+classId);
        System.out.println("学生列表ID:");
        for (Integer studentId : studentIds) {
            System.out.println("studentId:"+studentId);
        }
        int successCount = classStudentService.batchRemoveStudentsFromClass(classId, studentIds);
        return ResponseEntity.ok(successCount);
    }

    /**
     * 检查学生是否已选同课程的其他班级
     * GET /service/classStudent/checkSameCourse
     */
    @GetMapping("/checkSameCourse")
    public ResponseEntity<Boolean> hasStudentSelectedSameCourse(
            @RequestParam Integer studentId,
            @RequestParam Integer courseId) {
        System.out.println("根据查看当前学生是否已经选择了同课程的其他班级");
        System.out.println("学生Id:"+studentId);
        System.out.println("课程Id:"+courseId);
        boolean hasSelected = classStudentService.hasStudentSelectedSameCourse(studentId, courseId);
        return ResponseEntity.ok(hasSelected);
    }
}