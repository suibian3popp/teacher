package org.example.online.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.example.online.entity.SessionAttendance;

/**
* @author lenovo
* @description 针对表【session_attendance(学生参与记录表（仅记录学生）)】的数据库操作Service
* @createDate 2025-06-29 20:52:23
*/
public interface SessionAttendanceService extends IService<SessionAttendance> {

     SessionAttendance joinSession(int sessionId, int studentId);

     SessionAttendance leaveSession(int sessionId, int studentId);

     void incrementInteractionCount(int sessionId, int studentId);

}
