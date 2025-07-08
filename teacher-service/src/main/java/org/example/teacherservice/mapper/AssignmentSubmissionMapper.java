package org.example.teacherservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.teacherservice.entity.AssignmentSubmission;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AssignmentSubmissionMapper extends BaseMapper<AssignmentSubmission> {
    /**
     * 根据作业-班级关联ID查询提交ID列表
     * @param assignmentClassId 作业-班级关联ID
     * @return 提交ID列表
     */
    @Select("SELECT submission_id FROM assignment_submissions WHERE assignment_class_id = #{assignmentClassId}")
    List<Integer> selectSubmissionIdsByAssignmentClassId(@Param("assignmentClassId") Integer assignmentClassId);

    /**
     * 根据作业-班级关联ID查询完整的提交记录列表
     * @param assignmentClassId 作业-班级关联ID
     * @return 提交记录列表
     */
    @Select("SELECT * FROM assignment_submissions WHERE assignment_class_id = #{assignmentClassId}")
    List<AssignmentSubmission> selectByAssignmentClassId(@Param("assignmentClassId") Integer assignmentClassId);
}
