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
    }

