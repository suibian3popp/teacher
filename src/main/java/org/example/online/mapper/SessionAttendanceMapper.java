package org.example.online.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.online.entity.SessionAttendance;
import org.springframework.stereotype.Repository;

/**
* @author lenovo
* @description 针对表【session_attendance(学生参与记录表（仅记录学生）)】的数据库操作Mapper
* @createDate 2025-06-29 20:52:23
* @Entity live.entity.SessionAttendance
*/
@Mapper
public interface SessionAttendanceMapper extends BaseMapper<SessionAttendance> {

}




