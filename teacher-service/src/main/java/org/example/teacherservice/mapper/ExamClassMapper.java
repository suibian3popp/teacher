package org.example.teacherservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.teacherservice.entity.ExamClasses;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ExamClassMapper extends BaseMapper<ExamClasses> {
}
