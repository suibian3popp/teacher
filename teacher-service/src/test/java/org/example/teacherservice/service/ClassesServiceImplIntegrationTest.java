package org.example.teacherservice.service;

import static org.junit.jupiter.api.Assertions.*;

import org.example.teacherservice.entity.Classes;
import org.example.teacherservice.entity.StudentClass;
import org.example.teacherservice.service.impl.ClassServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional // 测试后自动回滚
public class ClassesServiceImplIntegrationTest {

    @Autowired
    private ClassServiceImpl classService;

    private Integer testCourseId = 1;
    private Classes testClasses;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        testClasses = new Classes();
        testClasses.setCourseId(testCourseId);
        testClasses.setName("Test Class");
        testClasses.setDescription("This is a test class");
    }

    @Test
    void createClass_ShouldSuccess_WhenInputValid() {
        // 执行
        Integer classId = classService.createClass(testClasses);

        // 验证
        assertNotNull(classId);
        Classes created = classService.getClassById(classId);
        assertEquals(testClasses.getName(), created.getName());
        assertEquals(testClasses.getCourseId(), created.getCourseId());
        assertEquals(0, created.getStudentCount()); // 初始人数应为0
    }

    @Test
    void createClass_ShouldThrowException_WhenNameIsEmpty() {
        testClasses.setName("");

        // 验证
        assertThrows(IllegalArgumentException.class, () -> {
            classService.createClass(testClasses);
        });
    }

    @Test
    void updateClass_ShouldSuccess_WhenInputValid() {
        // 准备
        Integer classId = classService.createClass(testClasses);
        Classes update = new Classes();
        update.setClassId(classId);
        update.setName("Updated Name");
        update.setDescription("Updated Description");

        // 执行
        classService.updateClass(update);

        // 验证
        Classes updated = classService.getClassById(classId);
        assertEquals("Updated Name", updated.getName());
        assertEquals("Updated Description", updated.getDescription());
        assertEquals(testClasses.getCourseId(), updated.getCourseId()); // courseId 应保持不变
    }

    @Test
    void deleteClass_ShouldSuccess_WhenClassExists() {
        // 准备
        Integer classId = classService.createClass(testClasses);

        // 执行
        classService.deleteClass(classId);

        // 验证
        assertThrows(RuntimeException.class, () -> {
            classService.getClassById(classId);
        });
    }

    @Test
    void getClassById_ShouldReturnClass_WhenClassExists() {
        // 准备
        Integer classId = classService.createClass(testClasses);

        // 执行
        Classes result = classService.getClassById(classId);

        // 验证
        assertNotNull(result);
        assertEquals(classId, result.getClassId());
    }

    @Test
    void getClassesByCourse_ShouldReturnList_WhenCourseHasClasses() {
        // 准备
        classService.createClass(testClasses);

        // 执行
        List<Classes> classes = classService.getClassesByCourse(testCourseId);

        // 验证
        assertFalse(classes.isEmpty());
        assertTrue(classes.stream().anyMatch(c -> c.getName().equals(testClasses.getName())));
    }

    @Test
    void searchClasses_ShouldReturnFilteredResults_WhenKeywordProvided() {
        // 准备
        classService.createClass(testClasses);
        Classes anotherClasses = new Classes();
        anotherClasses.setCourseId(testCourseId);
        anotherClasses.setName("Another Class");
        classService.createClass(anotherClasses);

        // 执行
        List<Classes> results = classService.searchClasses(testCourseId, "Test");

        // 验证
        assertEquals(1, results.size());
        assertEquals(testClasses.getName(), results.get(0).getName());
    }

    @Test
    void countClassesByCourse_ShouldReturnCorrectCount() {
        // 准备
        int initialCount = classService.countClassesByCourse(testCourseId);
        classService.createClass(testClasses);

        // 执行
        int newCount = classService.countClassesByCourse(testCourseId);

        // 验证
        assertEquals(initialCount + 1, newCount);
    }

    @Test
    void getClassStudents_ShouldReturnEmptyList_WhenNoStudents() {
        // 准备
        Integer classId = classService.createClass(testClasses);

        // 执行
        List<StudentClass> students = classService.getClassStudents(classId);

        // 验证
        assertTrue(students.isEmpty());
    }

    @Test
    void existsById_ShouldReturnTrue_WhenClassExists() {
        // 准备
        Integer classId = classService.createClass(testClasses);

        // 执行 & 验证
        assertTrue(classService.existsById(classId));
    }

    @Test
    void existsById_ShouldReturnFalse_WhenClassNotExists() {
        assertFalse(classService.existsById(-1));
    }
}
