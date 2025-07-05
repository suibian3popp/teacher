package org.demo_hd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.demo_hd.entity.Courses;
import org.demo_hd.service.CoursesService;
import org.demo_hd.mapper.CoursesMapper;
import org.springframework.stereotype.Service;

/**
* @author tang_
* @description 针对表【courses(课程信息表)】的数据库操作Service实现
* @createDate 2025-07-05 11:30:55
*/
@Service
public class CoursesServiceImpl extends ServiceImpl<CoursesMapper, Courses>
    implements CoursesService{

}




