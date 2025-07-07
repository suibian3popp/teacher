package org.example.teacherservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.teacherservice.entity.Chapters;
import org.springframework.stereotype.Repository;

/**
 * @author tang_
 * @description 针对表【chapters(课程章节表)】的数据库操作Mapper
 * @createDate 2025-07-05 11:02:48
 * @Entity org.demo_hd.entity.Chapters
 */
@Mapper
@Repository
public interface ChaptersMapper extends BaseMapper<Chapters> {

}
