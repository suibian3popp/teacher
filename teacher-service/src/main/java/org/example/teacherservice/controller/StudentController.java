package org.example.teacherservice.controller;

import org.example.teacherservice.entity.Student;
import org.example.teacherservice.entity.StudentClass;
import org.example.teacherservice.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 创建学生信息
     * POST /service/student
     */
    @PostMapping
    public ResponseEntity<Integer> createStudent(@RequestBody Student student) {
        System.out.println("创建学生信息");
        System.out.println("学生学号："+student.getStudentNo());
        System.out.println("学生院系Id"+student.getDepartmentId());
        System.out.println("学生真名"+student.getRealName());
        Integer studentId = studentService.createStudent(student);
        System.out.println("自动生成的学生账号ID"+studentId);
        return ResponseEntity.ok(studentId);
    }

    /**
     * 更新学生信息
     * PUT /service/student
     */
    @PutMapping
    public ResponseEntity<Void> updateStudent(@RequestBody Student student) {
        System.out.println("更新学生信息");
        System.out.println("学生学号:+"+student.getStudentNo());
        System.out.println("学生账号ID："+student.getStudentId());
        System.out.println("学生真名："+student.getRealName());
        studentService.updateStudent(student);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除学生
     * DELETE /service/student/{studentId}
     */
    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer studentId) {
        System.out.println("删除学生");
        System.out.println("学生账号ID："+studentId);
        studentService.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 获取学生详情
     * GET /service/student/{studentId}
     */
    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable Integer studentId) {
        Student student = studentService.getStudentById(studentId);
        System.out.println("获取学生详情");
        System.out.println("学生ID:"+studentId);
        return ResponseEntity.ok(student);
    }

    /**
     * 根据学号查询学生
     * GET /service/student/no/{studentNo}
     */
    @GetMapping("/no/{studentNo}")
    public ResponseEntity<Student> getStudentByNo(@PathVariable String studentNo) {
        System.out.println("根据学号查询学生");
        System.out.println("学生学号："+studentNo);
        Student student = studentService.getStudentByNo(studentNo);
        return ResponseEntity.ok(student);
    }

    /**
     * 学生选课
     * POST /service/student/course
     */
    @PostMapping("/course")
    public ResponseEntity<Void> selectCourse(@RequestBody StudentClass studentClass) {
        System.out.println("学生选课");
        System.out.println("学生选课关系");
        System.out.println("学生选择课程ID："+studentClass.getCourseId());
        System.out.println("学生ID:"+studentClass.getStudentId());
        System.out.println("学生选课的班级"+studentClass.getClassId());
        studentService.selectCourse(studentClass);
        return ResponseEntity.ok().build();
    }

    /**
     * 获取学生所选课程列表
     * GET /service/student/{studentId}/courses
     */
    @GetMapping("/{studentId}/courses")
    public ResponseEntity<List<StudentClass>> getStudentCourses(
            @PathVariable Integer studentId) {
        System.out.println("获取学生选课列表");
        List<StudentClass> courses = studentService.getStudentCourses(studentId);
        return ResponseEntity.ok(courses);
    }

    /**
     * 搜索学生（模糊查询）
     * GET /service/student/search?keyword=张三
     */
    @GetMapping("/search")
    public ResponseEntity<List<Student>> searchStudents(
            @RequestParam(required = false) String keyword) {
        System.out.println("搜索学生");
        List<Student> students = studentService.searchStudents(keyword);
        return ResponseEntity.ok(students);
    }

    /**
     * 根据院系搜索学生
     * GET /service/student/department/{departmentId}?keyword=张
     */
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Student>> searchStudentsByDepartment(
            @PathVariable Integer departmentId,
            @RequestParam(required = false) String keyword) {
        System.out.println("查看院系学生有哪些");
        System.out.println("院系ID:"+departmentId);
        List<Student> students = studentService.searchStudentsByDepartment(departmentId, keyword);
        return ResponseEntity.ok(students);
    }

    /**
     * 检查学生是否存在
     * GET /service/student/{studentId}/exists
     */
    @GetMapping("/{studentId}/exists")
    public ResponseEntity<Boolean> existsById(@PathVariable Integer studentId) {
        System.out.println("判断学生是否存在");
        System.out.println("学生Id:"+studentId);
        boolean exists = studentService.existsById(studentId);
        return ResponseEntity.ok(exists);
    }
}