package org.example.teacherservice.controller;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.example.teacherservice.entity.Classes;
import org.example.teacherservice.entity.StudentClass;
import org.example.teacherservice.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/service/classes")
public class ClassesController {

    @Autowired
    private ClassService classService;

    //传入参数
//    @Data
//    @TableName("classes")
//    public class Classes {
//        @TableId(value = "class_id", type = IdType.AUTO)
//        private Integer classId;
//
//        @TableField("course_id")
//        private Integer courseId;
//
//        @TableField("name")
//        private String name;
//
//        @TableField("description")
//        private String description;
//
//        @TableField("student_count")
//        private Integer studentCount;
//
//        @TableField(value = "created_at", fill = FieldFill.INSERT)
//        private Date createdAt;
//    }


    /**
     * 创建班级
     * POST /service/classes
     */
    @PostMapping
    public ResponseEntity<Integer> createClass(@RequestBody Classes clazz) {
        System.out.println("创建班级");
        System.out.println("班级名字："+clazz.getName());
        System.out.println("班级关联课程："+clazz.getCourseId());
        System.out.println("班级人数:"+clazz.getStudentCount());
        Integer classId = classService.createClass(clazz);
        return ResponseEntity.ok(classId);
    }

    /**
     * 更新班级信息
     * PUT /service/classes
     */
    @PutMapping
    public ResponseEntity<Void> updateClass(@RequestBody Classes clazz) {
        System.out.println("更新班级信息");
        System.out.println("班级名字"+clazz.getName());
        System.out.println("班级关联课程"+clazz.getCourseId());
        classService.updateClass(clazz);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除班级
     * DELETE /service/classes/{classId}
     */
    @DeleteMapping("/{classId}")
    public ResponseEntity<Void> deleteClass(@PathVariable Integer classId) {
        System.out.println("删除班级");
        System.out.println("班级ID:"+classId);
        classService.deleteClass(classId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 获取班级详情
     * GET /service/classes/{classId}
     */
    @GetMapping("/{classId}")
    public ResponseEntity<Classes> getClassById(@PathVariable Integer classId) {
        System.out.println("根据班级ID获取班级详情");
        System.out.println("班级ID:"+classId);
        Classes clazz = classService.getClassById(classId);
        return ResponseEntity.ok(clazz);
    }

    /**
     * 获取课程下的所有班级
     * GET /service/classes/course/{courseId}
     */
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Classes>> getClassesByCourse(@PathVariable Integer courseId) {
        System.out.println("获取当前课程下面的所有ID");
        System.out.println("课程ID:"+courseId);
        List<Classes> classes = classService.getClassesByCourse(courseId);
        return ResponseEntity.ok(classes);
    }

    /**
     * 获取班级学生列表
     * GET /service/classes/{classId}/students
     */
    @GetMapping("/{classId}/students")
    public ResponseEntity<List<StudentClass>> getClassStudents(@PathVariable Integer classId) {
        System.out.println("获取班级学生列表");
        System.out.println("班级ID:"+classId);
        List<StudentClass> students = classService.getClassStudents(classId);
        return ResponseEntity.ok(students);
    }

    /**
     * 搜索班级
     * GET /service/classes/search?courseId=1&keyword=数学
     */
    @GetMapping("/search")
    public ResponseEntity<List<Classes>> searchClasses(
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) String keyword) {
        System.out.println("搜索班级，模糊搜索");
        System.out.println("课程ID："+courseId);
        System.out.println("关键字；"+keyword);
        List<Classes> classes = classService.searchClasses(courseId, keyword);
        return ResponseEntity.ok(classes);
    }

    /**
     * 获取课程下的班级数量
     * GET /service/classes/count?courseId=1
     */
    @GetMapping("/count")
    public ResponseEntity<Integer> countClassesByCourse(
            @RequestParam Integer courseId) {
        System.out.println("获取当前课程下面的班级数量");
        System.out.println("课程ID:"+courseId);
        int count = classService.countClassesByCourse(courseId);
        return ResponseEntity.ok(count);
    }

    /**
     * 检查班级是否存在
     * GET /service/classes/{classId}/exists
     */
    @GetMapping("/{classId}/exists")
    public ResponseEntity<Boolean> existsById(@PathVariable Integer classId) {
        System.out.println("根据班级ID查看当前班级是否存在");
        System.out.println("班级ID:"+classId);
        boolean exists = classService.existsById(classId);
        return ResponseEntity.ok(exists);
    }
}