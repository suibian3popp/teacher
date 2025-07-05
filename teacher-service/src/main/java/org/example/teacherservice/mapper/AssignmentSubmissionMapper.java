package org.example.teacherservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.teacherservice.entity.AssignmentSubmission;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AssignmentSubmissionMapper extends BaseMapper<AssignmentSubmission> {
}
