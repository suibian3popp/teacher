package org.example.teacherservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.teacherservice.entity.AssignmentGrade;
import org.example.teacherservice.vo.assignment.AssignmentGradedCountDTO;
import org.example.teacherservice.vo.assignment.GradeDetailVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AssignmentGradeMapper extends BaseMapper<AssignmentGrade> {
    /**
     * 根据提交记录ID查询批改ID
     * @param submissionId 提交记录ID
     * @return 批改ID
     */
    @Select("SELECT grade_id FROM assignment_grades WHERE submission_id = #{submissionId}")
    Integer selectGradeIdBySubmissionId(@Param("submissionId") Integer submissionId);

    /**
     * 根据提交记录ID查询完整的批改记录
     * @param submissionId 提交记录ID
     * @return 批改记录
     */
    @Select("SELECT * FROM assignment_grades WHERE submission_id = #{submissionId}")
    AssignmentGrade selectBySubmissionId(@Param("submissionId") Integer submissionId);
    }

