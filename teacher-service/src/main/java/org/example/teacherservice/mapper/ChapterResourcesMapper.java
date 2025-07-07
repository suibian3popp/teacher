package org.example.teacherservice.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.teacherservice.entity.ChapterResources;
import org.springframework.stereotype.Repository;

/**
 * @author tang_
 * @description 针对表【chapter_resources(章节资源关联表)】的数据库操作Mapper
 * @createDate 2025-07-05 11:02:42
 * @Entity org.demo_hd.entity.ChapterResources
 */
@Mapper
@Repository
public interface ChapterResourcesMapper extends BaseMapper<ChapterResources> {

}
