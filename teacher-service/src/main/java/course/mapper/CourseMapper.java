package course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import course.entity.Course;
import org.apache.ibatis.annotations.Mapper;
 
@Mapper
public interface CourseMapper extends BaseMapper<Course> {
} 