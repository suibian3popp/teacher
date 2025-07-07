package org.example.teacherservice.service;

import org.example.teacherservice.vo.AssignmentClassVO;
import org.example.teacherservice.vo.ClassSimpleVO;
import org.example.teacherservice.exception.BusinessException;

import java.util.List;
import java.util.Map;

/**
 * 作业-班级关联管理服务
 * 处理作业与班级的关联关系建立、查询和解除
 */
public interface AssignmentClassesService {
    /**
     * 批量建立作业-班级关联关系
     * @param assignmentId 作业ID
     * @param classIds 班级ID列表
     * @throws BusinessException 当班级不存在或关联已存在时抛出
     */
    void batchAddClassesToAssignment(Integer assignmentId, List<Integer> classIds);

    /**
     * 移除作业与指定班级的关联
     * @param assignmentId 作业ID
     * @param classId 班级ID
     */
    void removeClassFromAssignment(Integer assignmentId, Integer classId);

    /**
     * 删除作业的所有班级关联
     * @param assignmentId 作业ID
     */
    void clearAssignmentClasses(Integer assignmentId);

    /**
     * 获取作业关联的所有班级信息
     * @param assignmentId 作业ID
     * @return 班级简单信息列表
     */
    List<ClassSimpleVO> getClassesByAssignment(Integer assignmentId);

    /**
     * 批量查询作业关联的班级名称
     * @param assignmentIds 作业ID列表
     * @return 作业ID->班级名称列表的映射
     */
    Map<Integer, List<String>> getClassNamesByAssignments(List<Integer> assignmentIds);
}
