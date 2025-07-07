package org.example.teacherservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.teacherservice.entity.Courses;
import org.springframework.stereotype.Repository;

/**
 * @author tang_
 * @description 针对表【courses(课程信息表)】的数据库操作Mapper
 * @createDate 2025-07-05 11:30:55
 * @Entity org.demo_hd.entity.Courses
 */
@Mapper
@Repository
public interface CoursesMapper extends BaseMapper<Courses> {

}
