package org.example.online.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.online.entity.LiveSession;

/**
* @author lenovo
* @description 针对表【live_session(直播课程表（由教师创建）)】的数据库操作Mapper
* @createDate 2025-06-29 20:52:01
* @Entity live.entity.LiveSession
*/
@Mapper
public interface LiveSessionMapper extends BaseMapper<LiveSession> {

}




