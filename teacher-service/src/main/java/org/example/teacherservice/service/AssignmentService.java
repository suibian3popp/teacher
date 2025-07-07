package org.example.teacherservice.service;

import org.apache.ibatis.annotations.Param;
import org.example.teacherservice.dto.assignment.AssignmentCreateDTO;
import org.example.teacherservice.dto.assignment.AssignmentUpdateDTO;
import org.example.teacherservice.response.PageParam;
import org.example.teacherservice.response.PageResult;
import org.example.teacherservice.vo.AssignmentDetailVO;
import org.example.teacherservice.vo.AssignmentVO;
import org.example.teacherservice.exception.BusinessException;
import org.example.teacherservice.vo.assignment.AssignmentBasicVO;
import org.example.teacherservice.vo.assignment.AssignmentResourceVO;
import org.example.teacherservice.vo.assignment.AssignmentSearchResult;

import java.util.List;

/**
 * 教师端作业基础管理服务
 * 负责作业的生命周期管理（创建/更新/删除）和基础查询
 */
public interface AssignmentService {

    /**
     * 创建作业基础信息
     * @param dto 包含作业标题、描述、总分值等基础信息
     * @param creatorId 创建人ID
     * @return 创建成功的作业ID
     */
    Integer createAssignment(AssignmentCreateDTO dto, Integer creatorId);

    /**
     * 更新作业基础信息
     * @param dto 包含需要更新的作业字段
     */
    void updateAssignment(AssignmentUpdateDTO dto);

    /**
     * 删除作业（需同时清理关联数据）
     * @param assignmentId 作业ID
     */
    void deleteAssignment(Integer assignmentId);

    /**
     * 获取作业基础信息
     * @param assignmentId 作业ID
     * @return 作业基础信息视图
     */
    AssignmentBasicVO getAssignmentBasicInfo(Integer assignmentId);

    /**
     * 检查作业是否存在
     * @param assignmentId 作业ID
     * @return 存在返回true，否则false
     */
    boolean existsById(Integer assignmentId);

    /**
     * 根据发布人ID搜索作业
     * @param creatorId 发布人ID
     * @param pageParam 分页参数
     * @return 作业分页列表
     */
    PageResult<AssignmentVO> searchByCreator(Integer creatorId, PageParam pageParam);

    /**
     * 根据关联资源ID搜索作业
     * @param resourceId 资源ID
     * @return 关联该资源的作业列表
     */
    List<AssignmentResourceVO> searchByResource(Integer resourceId);

    /**
     * 综合搜索作业（标题模糊搜索+发布人筛选）
     * @param titleKeyword 标题关键词（可选）
     * @param creatorId 发布人ID（可选）
     * @param status 状态（1-未开始 2-进行中 3-已截止）
     * @return 符合条件的作业列表
     */
    List<AssignmentSearchResult> searchAssignments(
            @Param("titleKeyword") String titleKeyword,
            @Param("creatorId") Integer creatorId,
            @Param("status") Integer status);
}