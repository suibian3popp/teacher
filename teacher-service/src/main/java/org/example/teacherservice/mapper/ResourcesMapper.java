package org.example.teacherservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.teacherservice.entity.Resources;
import org.springframework.stereotype.Repository;

/**
 * @author tang_
 * @description 针对表【resources(教学资源表)】的数据库操作Mapper
 * @createDate 2025-07-01 13:51:35
 * @Entity org.demo_hd.entity.Resources
 */
@Mapper
@Repository
public interface ResourcesMapper extends BaseMapper<Resources> {

}
