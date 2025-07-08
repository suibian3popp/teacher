package org.example.teacherservice.service;


/**
 * 单元测试
 */
//
//import org.example.teacherservice.dto.assignment.AssignmentCreateDTO;
//import org.example.teacherservice.dto.assignment.AssignmentUpdateDTO;
//import org.example.teacherservice.entity.Assignment;
//import org.example.teacherservice.exception.BusinessException;
//import org.example.teacherservice.mapper.AssignmentClassesMapper;
//import org.example.teacherservice.mapper.AssignmentMapper;
//import org.example.teacherservice.response.PageParam;
//import org.example.teacherservice.response.PageResult;
//import org.example.teacherservice.service.impl.AssignmentServiceImpl;
//import org.example.teacherservice.vo.assignment.AssignmentSearchResult;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.math.BigDecimal;
//import java.time.ZoneId;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.junit.jupiter.api.Assertions.*;
//
////@SpringBootTest
//@ExtendWith(MockitoExtension.class)
//public class AssignmentServiceImplTest {
//    @Mock
//    private AssignmentMapper assignmentMapper;
//
//    @Mock
//    private AssignmentClassesMapper assignmentClassesMapper;
//
//    @InjectMocks
//    private AssignmentServiceImpl assignmentService;
//
//    private Date futureDate;
//    private Date pastDate;
//
//    @BeforeEach
//    void setUp() {
//        // 设置测试用的时间数据
//        futureDate = new Date(System.currentTimeMillis() + 86400000); // 1天后
//        pastDate = new Date(System.currentTimeMillis() - 86400000); // 1天前
//    }
//
//    // 测试数据准备方法
//    private AssignmentCreateDTO createValidCreateDTO() {
//        AssignmentCreateDTO dto = new AssignmentCreateDTO();
//        dto.setTitle("Math Homework");
//        dto.setDescription("Complete exercises 1-10");
//        dto.setDeadline(futureDate);
//        dto.setTotalScore(new BigDecimal("100.00"));
//        dto.setResourceId(1);
//        dto.setClassIds(Arrays.asList(101, 102));
//        return dto;
//    }
//
//    private AssignmentUpdateDTO createValidUpdateDTO() {
//        AssignmentUpdateDTO dto = new AssignmentUpdateDTO();
//        dto.setAssignmentId(1);
//        dto.setTitle("Updated Math Homework");
//        dto.setDescription("Complete exercises 1-15");
//        dto.setDeadline(futureDate);
//        dto.setTotalScore(new BigDecimal("150.00"));
//        return dto;
//    }
//
//    private Assignment createMockAssignment() {
//        Assignment assignment = new Assignment();
//        assignment.setAssignmentId(1);
//        assignment.setTitle("Math Homework");
//        assignment.setDescription("Complete exercises 1-10");
//        assignment.setDeadline(futureDate);
//        assignment.setTotalScore(new BigDecimal("100.00"));
//        assignment.setResourceId(1);
//        assignment.setCreatorId(100);
//        assignment.setCreateTime(new Date());
//        return assignment;
//    }
//
//    // 测试方法开始
//    @Test
//    void createAssignment_Success() {
//        // 准备测试数据
//        AssignmentCreateDTO dto = createValidCreateDTO();
//        Integer creatorId = 100;
//
//        // 模拟Mapper行为
//        when(assignmentMapper.insert(any(Assignment.class))).thenAnswer(invocation -> {
//            Assignment assignment = invocation.getArgument(0);
//            assignment.setAssignmentId(1); // 模拟生成ID
//            return 1;
//        });
//
//        // 执行测试
//        Integer assignmentId = assignmentService.createAssignment(dto, creatorId);
//
//        // 验证结果
//        assertNotNull(assignmentId);
//        assertEquals(1, assignmentId);
//
//        // 验证Mapper调用
//        verify(assignmentMapper).insert(any(Assignment.class));
//        verify(assignmentClassesMapper, times(2)).insert(any()); // 两个classIds
//    }
//
//    @Test
//    void createAssignment_InvalidCreatorId_ThrowsException() {
//        // 准备测试数据
//        AssignmentCreateDTO dto = createValidCreateDTO();
//
//        // 测试无效的creatorId
//        assertThrows(BusinessException.class, () ->
//                assignmentService.createAssignment(dto, null));
//        assertThrows(BusinessException.class, () ->
//                assignmentService.createAssignment(dto, 0));
//
//        // 验证没有调用Mapper
//        verifyNoInteractions(assignmentMapper);
//        verifyNoInteractions(assignmentClassesMapper);
//    }
//
//    @Test
//    void createAssignment_NullDto_ThrowsException() {
//        assertThrows(BusinessException.class, () ->
//                assignmentService.createAssignment(null, 100));
//    }
//
//    @Test
//    void updateAssignment_Success() {
//        // 准备测试数据
//        AssignmentUpdateDTO dto = createValidUpdateDTO();
//        Assignment existing = createMockAssignment();
//
//        // 模拟Mapper行为
//        when(assignmentMapper.selectById(1)).thenReturn(existing);
//        when(assignmentMapper.updateById(any(Assignment.class))).thenReturn(1);
//
//        // 执行测试
//        assignmentService.updateAssignment(dto);
//
//        // 验证Mapper调用
//        verify(assignmentMapper).selectById(1);
//        verify(assignmentMapper).updateById(any(Assignment.class));
//    }
//
//    @Test
//    void updateAssignment_NotFound_ThrowsException() {
//        // 准备测试数据
//        AssignmentUpdateDTO dto = createValidUpdateDTO();
//
//        // 模拟Mapper行为
//        when(assignmentMapper.selectById(1)).thenReturn(null);
//
//        // 执行测试并验证异常
//        assertThrows(BusinessException.class, () ->
//                assignmentService.updateAssignment(dto));
//    }
//
//    @Test
//    void updateAssignment_InvalidId_ThrowsException() {
//        // 准备无效ID的DTO
//        AssignmentUpdateDTO dto = new AssignmentUpdateDTO();
//        dto.setAssignmentId(0);
//
//        // 执行测试并验证异常
//        assertThrows(BusinessException.class, () ->
//                assignmentService.updateAssignment(dto));
//
//        // 验证没有调用Mapper
//        verifyNoInteractions(assignmentMapper);
//    }
//
//    @Test
//    void deleteAssignment_Success() {
//        // 模拟Mapper行为
//        when(assignmentMapper.deleteById(1)).thenReturn(1);
//        when(assignmentClassesMapper.deleteByAssignmentId(1)).thenReturn(2);
//
//        // 执行测试
//        assignmentService.deleteAssignment(1);
//
//        // 验证Mapper调用顺序
//        verify(assignmentClassesMapper).deleteByAssignmentId(1);
//        verify(assignmentMapper).deleteById(1);
//    }
//
//    @Test
//    void deleteAssignment_InvalidId_ThrowsException() {
//        assertThrows(BusinessException.class, () ->
//                assignmentService.deleteAssignment(null));
//        assertThrows(BusinessException.class, () ->
//                assignmentService.deleteAssignment(0));
//
//        // 验证没有调用Mapper
//        verifyNoInteractions(assignmentMapper);
//        verifyNoInteractions(assignmentClassesMapper);
//    }
//
//    @Test
//    void getAssignmentBasicInfo_Success() {
//        // 准备测试数据
//        Assignment assignment = createMockAssignment();
//
//        // 模拟Mapper行为
//        when(assignmentMapper.selectById(1)).thenReturn(assignment);
//
//        // 执行测试
//        var result = assignmentService.getAssignmentBasicInfo(1);
//
//        // 验证结果
//        assertNotNull(result);
//        assertEquals("Math Homework", result.getTitle());
//        assertEquals(new BigDecimal("100.00"), result.getTotalScore());
//
//        // 验证Mapper调用
//        verify(assignmentMapper).selectById(1);
//    }
//
//    @Test
//    void getAssignmentBasicInfo_NotFound_ThrowsException() {
//        // 模拟Mapper行为
//        when(assignmentMapper.selectById(1)).thenReturn(null);
//
//        // 执行测试并验证异常
//        assertThrows(BusinessException.class, () ->
//                assignmentService.getAssignmentBasicInfo(1));
//    }
//
//    @Test
//    void existsById_ReturnsCorrectBoolean() {
//        // 模拟Mapper行为
//        when(assignmentMapper.selectById(1)).thenReturn(new Assignment());
//        when(assignmentMapper.selectById(2)).thenReturn(null);
//
//        // 执行测试并验证结果
//        assertTrue(assignmentService.existsById(1));
//        assertFalse(assignmentService.existsById(2));
//
//        // 验证Mapper调用
//        verify(assignmentMapper, times(2)).selectById(anyInt());
//    }
//
//    @Test
//    void searchByCreator_Success() {
//        // 准备测试数据
//        PageParam pageParam = new PageParam();
//        pageParam.setPageNum(1);
//        pageParam.setPageSize(10);
//
//        Assignment assignment = createMockAssignment();
//        List<Assignment> assignments = Collections.singletonList(assignment);
//
//        // 模拟Mapper行为
//        when(assignmentMapper.countByCreatorId(100)).thenReturn(1L);
//        when(assignmentMapper.selectByCreatorId(eq(100), anyInt(), anyInt()))
//                .thenReturn(assignments);
//
//        // 执行测试
//        PageResult<?> result = assignmentService.searchByCreator(100, pageParam);
//
//        // 验证结果
//        assertNotNull(result);
//        assertEquals(1, result.getTotal());
//        assertEquals(1, result.getList().size());
//
//        // 验证Mapper调用
//        verify(assignmentMapper).countByCreatorId(100);
//        verify(assignmentMapper).selectByCreatorId(eq(100), anyInt(), anyInt());
//    }
//
//    @Test
//    void searchByCreator_InvalidCreatorId_ThrowsException() {
//        assertThrows(BusinessException.class, () ->
//                assignmentService.searchByCreator(null, new PageParam()));
//        assertThrows(BusinessException.class, () ->
//                assignmentService.searchByCreator(0, new PageParam()));
//    }
//
//    @Test
//    void searchByResource_Success() {
//        // 准备测试数据
//        Assignment assignment = createMockAssignment();
//        List<Assignment> assignments = Collections.singletonList(assignment);
//
//        // 模拟Mapper行为
//        when(assignmentMapper.selectByResourceId(1)).thenReturn(assignments);
//
//        // 执行测试
//        List<?> result = assignmentService.searchByResource(1);
//
//        // 验证结果
//        assertNotNull(result);
//        assertEquals(1, result.size());
//
//        // 验证Mapper调用
//        verify(assignmentMapper).selectByResourceId(1);
//    }
//
//    @Test
//    void searchByResource_InvalidResourceId_ThrowsException() {
//        assertThrows(BusinessException.class, () ->
//                assignmentService.searchByResource(null));
//        assertThrows(BusinessException.class, () ->
//                assignmentService.searchByResource(0));
//    }
//
//    @Test
//    void searchAssignments_Success() {
//        // 准备测试数据
//        Assignment assignment = createMockAssignment();
//        List<AssignmentSearchResult> expected = Collections.singletonList(
//                new AssignmentSearchResult() {{
//                    setAssignmentId(assignment.getAssignmentId());
//                    setTitle(assignment.getTitle());
//                    setCreatorName("Test Teacher"); // 假设的教师姓名
//                    setStatus(2); // 假设状态为"进行中"
//                    setDeadline(assignment.getDeadline().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
//                }}
//        );
//
//        // 模拟Mapper行为
//        when(assignmentMapper.searchAssignments(anyString(), anyInt(), anyInt()))
//                .thenReturn(expected);
//
//        // 执行测试
//        List<AssignmentSearchResult> result = assignmentService.searchAssignments("Math", 100, 1);
//
//        // 验证结果
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals(1, result.get(0).getAssignmentId());
//        assertEquals("Math Homework", result.get(0).getTitle());
//        assertEquals(2, result.get(0).getStatus());
//
//        // 验证Mapper调用
//        verify(assignmentMapper).searchAssignments("Math", 100, 1);
//    }
//
//    @Test
//    void searchAssignments_InvalidStatus_ThrowsException() {
//        assertThrows(IllegalArgumentException.class, () ->
//                assignmentService.searchAssignments(null, null, 4));
//    }
//}


import org.example.teacherservice.TeacherServiceApplication;
import org.example.teacherservice.dto.assignment.AssignmentCreateDTO;
import org.example.teacherservice.dto.assignment.AssignmentUpdateDTO;
import org.example.teacherservice.entity.Assignment;
import org.example.teacherservice.entity.AssignmentClasses;
import org.example.teacherservice.entity.Users;
import org.example.teacherservice.exception.BusinessException;
import org.example.teacherservice.mapper.AssignmentClassesMapper;
import org.example.teacherservice.mapper.AssignmentMapper;
import org.example.teacherservice.mapper.ClassesMapper;
import org.example.teacherservice.mapper.UsersMapper;
import org.example.teacherservice.response.PageParam;
import org.example.teacherservice.response.PageResult;
import org.example.teacherservice.service.impl.AssignmentServiceImpl;
import org.example.teacherservice.vo.ClassSimpleVO;
import org.example.teacherservice.vo.assignment.AssignmentSearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TeacherServiceApplication.class)
@ActiveProfiles("test")
@Transactional
public class AssignmentServiceImplTest {

    @Autowired
    private AssignmentServiceImpl assignmentService;

    @Autowired
    private AssignmentMapper assignmentMapper;

    @Autowired
    private AssignmentClassesMapper assignmentClassesMapper;
    @Autowired
    private UsersMapper usersMapper;

    // Mock ClassesMapper，让测试认为 class_id=101 和 102 存在
    @MockBean
    private ClassesMapper classesMapper;  // 假设你有 ClassesMapper

    private Date futureDate;
    private Date pastDate;

    @BeforeEach
    void setUp() {
        // 清空测试数据

        assignmentMapper.delete(null);
        assignmentClassesMapper.delete(null);

        // 设置测试用的时间数据
        futureDate = new Date(System.currentTimeMillis() + 86400000); // 1天后
        pastDate = new Date(System.currentTimeMillis() - 86400000); // 1天前
    }

    // 测试数据准备方法
    private AssignmentCreateDTO createValidCreateDTO() {
        AssignmentCreateDTO dto = new AssignmentCreateDTO();
        dto.setTitle("Math Homework");
        dto.setDescription("Complete exercises 1-10");
        dto.setDeadline(futureDate);
        dto.setTotalScore(new BigDecimal("100.00"));
        dto.setResourceId(1);
        dto.setClassIds(Arrays.asList(101, 102));
        return dto;
    }

    private AssignmentUpdateDTO createValidUpdateDTO() {
        AssignmentUpdateDTO dto = new AssignmentUpdateDTO();
        dto.setAssignmentId(1);
        dto.setTitle("Updated Math Homework");
        dto.setDescription("Complete exercises 1-15");
        dto.setDeadline(futureDate);
        dto.setTotalScore(new BigDecimal("150.00"));
        return dto;
    }
    @Test
    void testUserExists() {
        Users user = usersMapper.selectById(1); // 确认能查到
        assertNotNull(user, "用户1必须存在");
    }

    @Test
    void createAssignment_Success() {
        // 准备测试数据
        AssignmentCreateDTO dto = createValidCreateDTO();
        Integer creatorId = 1;

        // 执行测试
        Integer assignmentId = assignmentService.createAssignment(dto, creatorId);

        // 验证数据库记录
        Assignment created = assignmentMapper.selectById(assignmentId);
        assertNotNull(created);
        assertEquals("Math Homework", created.getTitle());
        assertEquals(creatorId, created.getCreatorId());

        // 修改这里：使用正确的返回类型（假设返回的是ClassSimpleVO列表）
        List<ClassSimpleVO> classes = assignmentClassesMapper.selectClassesByAssignmentId(assignmentId);
        assertEquals(2, classes.size());
        assertEquals(101, classes.get(0).getClassId());
        assertEquals(102, classes.get(1).getClassId());
    }

    @Test
    void createAssignment_InvalidCreatorId_ThrowsException() {
        // 准备测试数据
        AssignmentCreateDTO dto = createValidCreateDTO();

        // 测试无效的creatorId
        assertThrows(BusinessException.class, () ->
                assignmentService.createAssignment(dto, null));
        assertThrows(BusinessException.class, () ->
                assignmentService.createAssignment(dto, 0));

        // 验证数据库没有插入记录
        assertEquals(0, assignmentMapper.selectCount(null));
    }

    @Test
    void updateAssignment_Success() {
        // 先创建测试数据
        AssignmentCreateDTO createDTO = createValidCreateDTO();
        Integer assignmentId = assignmentService.createAssignment(createDTO, 1);

        // 准备更新数据
        AssignmentUpdateDTO updateDTO = new AssignmentUpdateDTO();
        updateDTO.setAssignmentId(assignmentId);
        updateDTO.setTitle("Updated Title");
        updateDTO.setDescription("Updated Description");
        updateDTO.setDeadline(pastDate);
        updateDTO.setTotalScore(new BigDecimal("200.00"));

        // 执行更新
        assignmentService.updateAssignment(updateDTO);

        // 验证更新结果
        Assignment updated = assignmentMapper.selectById(assignmentId);
        assertEquals("Updated Title", updated.getTitle());
        assertEquals("Updated Description", updated.getDescription());
//        assertEquals(pastDate, updated.getDeadline());
        assertEquals(new BigDecimal("200.00"), updated.getTotalScore());
    }

    @Test
    void deleteAssignment_Success() {
        // 先创建测试数据
        AssignmentCreateDTO createDTO = createValidCreateDTO();
        Integer assignmentId = assignmentService.createAssignment(createDTO, 1);

        // 验证关联数据存在
        assertEquals(2, assignmentClassesMapper.selectClassesByAssignmentId(assignmentId).size());

        // 执行删除
        assignmentService.deleteAssignment(assignmentId);

        // 验证数据已删除
        assertNull(assignmentMapper.selectById(assignmentId));
        assertEquals(0, assignmentClassesMapper.selectClassesByAssignmentId(assignmentId).size());
    }

    @Test
    void getAssignmentBasicInfo_Success() {
        // 先创建测试数据
        AssignmentCreateDTO createDTO = createValidCreateDTO();
        Integer assignmentId = assignmentService.createAssignment(createDTO, 1);

        // 执行查询
        var result = assignmentService.getAssignmentBasicInfo(assignmentId);

        // 验证结果
        assertNotNull(result);
        assertEquals("Math Homework", result.getTitle());
        assertEquals(new BigDecimal("100.00"), result.getTotalScore());
    }

    @Test
    void searchByCreator_Success() {
        // 创建多个测试数据
        for (int i = 0; i < 5; i++) {
            AssignmentCreateDTO dto = createValidCreateDTO();
            dto.setTitle("Assignment " + i);
            assignmentService.createAssignment(dto, 1);
        }

        // 设置分页参数
        PageParam pageParam = new PageParam();
        pageParam.setPageNum(1);
        pageParam.setPageSize(3);

        // 执行查询
        PageResult<?> result = assignmentService.searchByCreator(1, pageParam);

        // 验证结果
        assertEquals(5, result.getTotal());
        assertEquals(3, result.getList().size());
    }

    @Test
    void searchByResource_Success() {
        // 创建测试数据
        AssignmentCreateDTO dto1 = createValidCreateDTO();
        dto1.setResourceId(1);
        assignmentService.createAssignment(dto1, 1);

        AssignmentCreateDTO dto2 = createValidCreateDTO();
        dto2.setResourceId(2);
        assignmentService.createAssignment(dto2, 1);

        // 执行查询
        List<?> result = assignmentService.searchByResource(1);

        // 验证结果
        assertEquals(1, result.size());
    }

    @Test
    void searchAssignments_Success() {
        // 创建不同状态的作业
        createAssignmentWithDeadline("Future Assignment", futureDate); // 未开始
        createAssignmentWithDeadline("Current Assignment", new Date()); // 进行中
        createAssignmentWithDeadline("Past Assignment", pastDate); // 已截止

        // 测试按状态查询
        List<AssignmentSearchResult> futureResults = assignmentService.searchAssignments(null, null, 1);
        assertEquals(1, futureResults.size());
        assertEquals("Future Assignment", futureResults.get(0).getTitle());

        List<AssignmentSearchResult> pastResults = assignmentService.searchAssignments(null, null, 3);
        assertEquals(1, pastResults.size());
        assertEquals("Past Assignment", pastResults.get(0).getTitle());
    }

    private void createAssignmentWithDeadline(String title, Date deadline) {
        AssignmentCreateDTO dto = createValidCreateDTO();
        dto.setTitle(title);
        dto.setDeadline(deadline);
        assignmentService.createAssignment(dto, 1);
    }


}