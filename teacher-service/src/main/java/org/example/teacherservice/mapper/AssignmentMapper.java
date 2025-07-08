package org.example.teacherservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.teacherservice.entity.Assignment;
import org.example.teacherservice.vo.assignment.AssignmentSearchResult;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AssignmentMapper extends BaseMapper<Assignment> {
    Long countByCreatorId(@Param("creatorId") Integer creatorId);

    List<Assignment> selectByCreatorId(
            @Param("creatorId") Integer creatorId,
            @Param("offset") Integer offset,
            @Param("pageSize") Integer pageSize
    );

    List<Assignment> selectByResourceId(@Param("resourceId") Integer resourceId);

    List<AssignmentSearchResult> searchAssignments(
            @Param("titleKeyword") String titleKeyword,
            @Param("creatorId") Integer creatorId,
            @Param("status") Integer status
    );
}
