package org.example.teacherservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.teacherservice.entity.Courses;
import org.example.teacherservice.mapper.CoursesMapper;
import org.example.teacherservice.service.CoursesService;
import org.springframework.stereotype.Service;

/**
 * @author tang_
 * @description 针对表【courses(课程信息表)】的数据库操作Service实现
 * @createDate 2025-07-05 11:30:55
 */
@Service
public class CoursesServiceImpl extends ServiceImpl<CoursesMapper, Courses>
        implements CoursesService {

}