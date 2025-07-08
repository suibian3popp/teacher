package org.example.teacherservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.teacherservice.entity.Classes;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ClassesMapper extends BaseMapper<Classes> {
}
