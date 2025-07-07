package org.example.teacherservice.service.impl;

import org.example.teacherservice.dto.assignment.AssignmentCreateDTO;
import org.example.teacherservice.dto.assignment.AssignmentUpdateDTO;
import org.example.teacherservice.entity.Assignment;
import org.example.teacherservice.exception.BusinessException;
import org.example.teacherservice.mapper.AssignmentClassesMapper;
import org.example.teacherservice.mapper.AssignmentMapper;
import org.example.teacherservice.response.PageParam;
import org.example.teacherservice.response.PageResult;
import org.example.teacherservice.service.AssignmentService;
import org.example.teacherservice.vo.AssignmentVO;
import org.example.teacherservice.vo.assignment.AssignmentBasicVO;
import org.example.teacherservice.vo.assignment.AssignmentResourceVO;
import org.example.teacherservice.vo.assignment.AssignmentSearchResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    @Autowired
    private AssignmentMapper assignmentMapper;
    @Autowired
    private AssignmentClassesMapper assignmentClassesMapper;


    @Override
    @Transactional
    public Integer createAssignment(AssignmentCreateDTO dto, Integer creatorId) {
        if (dto == null) {
            throw new BusinessException("创建作业参数不能为空");
        }
        if (creatorId == null || creatorId <= 0) {
            throw new BusinessException("创建人ID不合法");
        }

        Assignment assignment = new Assignment();
        BeanUtils.copyProperties(dto, assignment);
        assignment.setCreatorId(creatorId);
        assignment.setCreateTime(new Date());

        assignmentMapper.insert(assignment);
        return assignment.getAssignmentId();
    }

    @Override
    @Transactional
    public void updateAssignment(AssignmentUpdateDTO dto) {
        if (dto == null) {
            throw new BusinessException("更新作业参数不能为空");
        }
        if (dto.getAssignmentId() == null || dto.getAssignmentId() <= 0) {
            throw new BusinessException("作业ID不合法");
        }

        Assignment existingAssignment = assignmentMapper.selectById(dto.getAssignmentId());
        if (existingAssignment == null) {
            throw new BusinessException("作业不存在");
        }

        Assignment assignment = new Assignment();
        assignment.setAssignmentId(dto.getAssignmentId());
        assignment.setTitle(StringUtils.hasText(dto.getTitle()) ? dto.getTitle() : existingAssignment.getTitle());
        assignment.setDescription(StringUtils.hasText(dto.getDescription()) ? dto.getDescription() : existingAssignment.getDescription());
        assignment.setDeadline(dto.getDeadline() != null ? dto.getDeadline() : existingAssignment.getDeadline());
        assignment.setTotalScore(dto.getTotalScore() != null ? dto.getTotalScore() : existingAssignment.getTotalScore());

        assignmentMapper.updateById(assignment);
    }

    @Override
    @Transactional
    public void deleteAssignment(Integer assignmentId) {
        if (assignmentId == null || assignmentId <= 0) {
            throw new BusinessException("作业ID不合法");
        }

        // 先删除关联表数据
        assignmentClassesMapper.deleteByAssignmentId(assignmentId);
        // 再删除作业
        assignmentMapper.deleteById(assignmentId);
    }

    @Override
    public AssignmentBasicVO getAssignmentBasicInfo(Integer assignmentId) {
        if (assignmentId == null || assignmentId <= 0) {
            throw new BusinessException("作业ID不合法");
        }

        Assignment assignment = assignmentMapper.selectById(assignmentId);
        if (assignment == null) {
            throw new BusinessException("作业不存在");
        }

        AssignmentBasicVO vo = new AssignmentBasicVO();
        BeanUtils.copyProperties(assignment, vo);
        return vo;
    }

    @Override
    public boolean existsById(Integer assignmentId) {
        if (assignmentId == null || assignmentId <= 0) {
            return false;
        }
        return assignmentMapper.selectById(assignmentId) != null;
    }

    @Override
    public PageResult<AssignmentVO> searchByCreator(Integer creatorId, PageParam pageParam) {
        if (creatorId == null || creatorId <= 0) {
            throw new BusinessException("创建人ID不合法");
        }
        if (pageParam == null) {
            pageParam = new PageParam();
            pageParam.setPageNum(1);
            pageParam.setPageSize(10);
        }

        // 计算偏移量
        int offset = (pageParam.getPageNum() - 1) * pageParam.getPageSize();

        // 获取总数
        Long total = assignmentMapper.countByCreatorId(creatorId);

        // 获取分页结果
        List<Assignment> assignments = assignmentMapper.selectByCreatorId(
                creatorId,
                offset,
                pageParam.getPageSize()
        );

        // 转换为VO列表
        List<AssignmentVO> voList = assignments.stream()
                .map(assignment -> {
                    AssignmentVO vo = new AssignmentVO();
                    BeanUtils.copyProperties(assignment, vo);
                    return vo;
                })
                .collect(Collectors.toList());

        // 构建分页结果
        PageResult<AssignmentVO> result = new PageResult<>();
        result.setTotal(total);
        result.setPageSize(pageParam.getPageSize());
        result.setPageNum(pageParam.getPageNum());
        result.setList(voList);

        return result;
    }

    @Override
    public List<AssignmentResourceVO> searchByResource(Integer resourceId) {
        if (resourceId == null || resourceId <= 0) {
            throw new BusinessException("资源ID不合法");
        }

        List<Assignment> assignments = assignmentMapper.selectByResourceId(resourceId);

        return assignments.stream()
                .map(assignment -> {
                    AssignmentResourceVO vo = new AssignmentResourceVO();
                    BeanUtils.copyProperties(assignment, vo);
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<AssignmentSearchResult> searchAssignments(String titleKeyword, Integer creatorId, Integer status) {
        return assignmentMapper.searchAssignments(titleKeyword, creatorId, status);
    }
}