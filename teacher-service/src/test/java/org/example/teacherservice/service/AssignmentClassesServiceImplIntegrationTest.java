package org.example.teacherservice.service;

import org.example.teacherservice.TeacherServiceApplication;
import org.example.teacherservice.entity.Assignment;
import org.example.teacherservice.entity.Classes;
import org.example.teacherservice.exception.BusinessException;
import org.example.teacherservice.mapper.AssignmentClassesMapper;
import org.example.teacherservice.mapper.AssignmentMapper;
import org.example.teacherservice.service.impl.AssignmentClassesServiceImpl;
import org.example.teacherservice.vo.ClassSimpleVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TeacherServiceApplication.class)
@Transactional
public class AssignmentClassesServiceImplIntegrationTest {

    @Autowired
    private AssignmentClassesServiceImpl assignmentClassesService;

    @Autowired
    private AssignmentClassesMapper assignmentClassesMapper;

    @Autowired
    private AssignmentMapper assignmentMapper;

    @Autowired  // 改为真实注入，不再使用Mock
    private ClassService classService;

    private Integer testAssignmentId;
    private List<Integer> testClassIds;

    @BeforeEach
    void setUp() {
        // 创建测试作业
        Assignment assignment = new Assignment();
        assignment.setTitle("测试作业");
        assignment.setDescription("测试说明");
        assignment.setTotalScore(BigDecimal.valueOf(100.0));
        assignment.setCreatorId(1);
        assignmentMapper.insert(assignment);
        testAssignmentId = assignment.getAssignmentId();

        // 创建测试班级
        testClassIds = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Classes clazz = new Classes();
            clazz.setName("测试班级" + i);
            clazz.setCourseId(1);  // 假设课程ID为1
            clazz.setDescription("测试班级描述");
            testClassIds.add(classService.createClass(clazz));
        }
    }

    @Test
    void batchAddClassesToAssignment_ShouldSuccess_WhenInputValid() {
        // 执行
        assignmentClassesService.batchAddClassesToAssignment(testAssignmentId, testClassIds);

        // 验证
        List<ClassSimpleVO> classes = assignmentClassesService.getClassesByAssignment(testAssignmentId);
        assertEquals(testClassIds.size(), classes.size());
        assertTrue(classes.stream()
                .map(ClassSimpleVO::getClassId)
                .collect(Collectors.toList())
                .containsAll(testClassIds));
    }

    @Test
    void batchAddClassesToAssignment_ShouldThrowException_WhenClassNotExist() {
        // 准备不存在的班级ID
        List<Integer> invalidClassIds = new ArrayList<>(testClassIds);
        invalidClassIds.add(999); // 不存在的班级ID

        // 执行和验证
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            assignmentClassesService.batchAddClassesToAssignment(testAssignmentId, invalidClassIds);
        });

        assertTrue(exception.getMessage().contains("以下班级不存在"));
    }

    @Test
    void batchAddClassesToAssignment_ShouldThrowException_WhenAssociationExists() {
        // 先添加一次
        assignmentClassesService.batchAddClassesToAssignment(testAssignmentId, testClassIds);

        // 尝试再次添加相同的关联
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            assignmentClassesService.batchAddClassesToAssignment(testAssignmentId, testClassIds);
        });

        assertTrue(exception.getMessage().contains("关联已存在"));
    }

    @Test
    void removeClassFromAssignment_ShouldSuccess_WhenAssociationExists() {
        // 先添加关联
        assignmentClassesService.batchAddClassesToAssignment(testAssignmentId, testClassIds);

        // 执行移除
        assignmentClassesService.removeClassFromAssignment(testAssignmentId, testClassIds.get(0));

        // 验证
        List<ClassSimpleVO> classes = assignmentClassesService.getClassesByAssignment(testAssignmentId);
        assertEquals(testClassIds.size() - 1, classes.size());
        assertFalse(classes.stream()
                .map(ClassSimpleVO::getClassId)
                .collect(Collectors.toList())
                .contains(testClassIds.get(0)));
    }

    @Test
    void removeClassFromAssignment_ShouldThrowException_WhenAssociationNotExists() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            assignmentClassesService.removeClassFromAssignment(testAssignmentId, 999);
        });

        assertTrue(exception.getMessage().contains("关联不存在"));
    }

    @Test
    void clearAssignmentClasses_ShouldRemoveAllAssociations() {
        // 先添加关联
        assignmentClassesService.batchAddClassesToAssignment(testAssignmentId, testClassIds);

        // 执行清空
        assignmentClassesService.clearAssignmentClasses(testAssignmentId);

        // 验证
        List<ClassSimpleVO> classes = assignmentClassesService.getClassesByAssignment(testAssignmentId);
        assertTrue(classes.isEmpty());
    }

    @Test
    void getClassesByAssignment_ShouldReturnEmptyList_WhenNoAssociations() {
        List<ClassSimpleVO> classes = assignmentClassesService.getClassesByAssignment(testAssignmentId);
        assertTrue(classes.isEmpty());
    }

    @Test
    void getClassNamesByAssignments_ShouldReturnCorrectMapping() {
        // 创建第二个作业
        Assignment assignment2 = new Assignment();
        assignment2.setTitle("作业2");
        assignment2.setDescription("作业2说明");
        assignment2.setTotalScore(BigDecimal.valueOf(100.0));
        assignment2.setCreatorId(1);
        assignmentMapper.insert(assignment2);
        Integer assignmentId2 = assignment2.getAssignmentId();

        // 为两个作业添加班级关联
        List<Integer> classIds1 = testClassIds.subList(0, 2); // 前两个班级
        List<Integer> classIds2 = Collections.singletonList(testClassIds.get(2)); // 最后一个班级

        assignmentClassesService.batchAddClassesToAssignment(testAssignmentId, classIds1);
        assignmentClassesService.batchAddClassesToAssignment(assignmentId2, classIds2);

        // 执行查询
        Map<Integer, List<String>> result = assignmentClassesService.getClassNamesByAssignments(
                Arrays.asList(testAssignmentId, assignmentId2));

        // 验证
        assertEquals(2, result.size());
        assertEquals(2, result.get(testAssignmentId).size());
        assertEquals(1, result.get(assignmentId2).size());
    }

    @Test
    void getClassNamesByAssignments_ShouldReturnEmptyMap_WhenInputEmpty() {
        Map<Integer, List<String>> result = assignmentClassesService.getClassNamesByAssignments(Collections.emptyList());
        assertTrue(result.isEmpty());
    }
}