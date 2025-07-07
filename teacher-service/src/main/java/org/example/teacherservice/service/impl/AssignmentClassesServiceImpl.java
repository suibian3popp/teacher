package org.example.teacherservice.service.impl;

import org.example.teacherservice.mapper.AssignmentMapper;
import org.example.teacherservice.mapper.AssignmentClassesMapper;
import org.example.teacherservice.service.AssignmentClassesService;
import org.example.teacherservice.service.ClassService;
import org.example.teacherservice.vo.ClassSimpleVO;
import org.example.teacherservice.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssignmentClassesServiceImpl implements AssignmentClassesService {

    @Autowired
    private AssignmentMapper assignmentMapper;

    @Autowired
    private AssignmentClassesMapper assignmentClassesMapper;

    @Autowired
    private ClassService classService;

    @Override
    @Transactional
    public void batchAddClassesToAssignment(Integer assignmentId, List<Integer> classIds) {
        if (assignmentId == null || classIds == null || classIds.isEmpty()) {
            throw new BusinessException("作业ID和班级ID列表不能为空");
        }

        // 验证所有班级是否存在
        List<Integer> nonExistingClasses = classIds.stream()
                .filter(classId -> !classService.existsById(classId))
                .collect(Collectors.toList());

        if (!nonExistingClasses.isEmpty()) {
            throw new BusinessException("以下班级不存在: " + nonExistingClasses);
        }

        // 检查哪些关联已存在
        List<Integer> existingClassIds = assignmentClassesMapper
                .selectExistingClassIds(assignmentId, classIds);

        if (!existingClassIds.isEmpty()) {
            throw new BusinessException("作业与以下班级的关联已存在: " + existingClassIds);
        }

        // 设置默认截止时间和发布状态
        Date deadline = new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000); // 默认7天后截止
        Integer publishStatus = 0; // 默认未发布

        // 批量插入新关联
        int result = assignmentClassesMapper.batchInsert(assignmentId, classIds, deadline, publishStatus);
        if (result != classIds.size()) {
            throw new BusinessException("批量添加班级关联失败");
        }
    }

    @Override
    @Transactional
    public void removeClassFromAssignment(Integer assignmentId, Integer classId) {
        if (assignmentId == null || classId == null) {
            throw new BusinessException("作业ID和班级ID不能为空");
        }

        int affectedRows = assignmentClassesMapper.deleteByAssignmentAndClass(assignmentId, classId);
        if (affectedRows == 0) {
            throw new BusinessException("指定的作业-班级关联不存在");
        }
    }

    @Override
    @Transactional
    public void clearAssignmentClasses(Integer assignmentId) {
        if (assignmentId == null) {
            throw new BusinessException("作业ID不能为空");
        }

        assignmentClassesMapper.deleteByAssignmentId(assignmentId);
    }

    @Override
    public List<ClassSimpleVO> getClassesByAssignment(Integer assignmentId) {
        if (assignmentId == null) {
            return Collections.emptyList();
        }

        return assignmentClassesMapper.selectClassesByAssignmentId(assignmentId);
    }

    @Override
    public Map<Integer, List<String>> getClassNamesByAssignments(List<Integer> assignmentIds) {
        if (assignmentIds == null || assignmentIds.isEmpty()) {
            return Collections.emptyMap();
        }

        // 查询所有相关的作业-班级名称映射
        List<Map<String, Object>> results = assignmentClassesMapper
                .selectClassNamesByAssignmentIds(assignmentIds);

        // 转换为Map<Integer, List<String>>结构
        Map<Integer, List<String>> resultMap = new HashMap<>();
        for (Map<String, Object> row : results) {
            Integer assignmentId = (Integer) row.get("assignmentId");
            String className = (String) row.get("className");

            resultMap.computeIfAbsent(assignmentId, k -> new ArrayList<>())
                    .add(className);
        }

        return resultMap;
    }
}