package org.example.teacherservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.teacherservice.entity.SessionAttendance;
import org.example.teacherservice.exception.ResourceNotFoundException;
import org.example.teacherservice.mapper.SessionAttendanceMapper;
import org.example.teacherservice.service.SessionAttendanceService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

/**
 * @author lenovo
 * @description 针对表【session_attendance(学生参与记录表（仅记录学生）)】的数据库操作Service实现
 * @createDate 2025-06-29 20:52:23
 */
@Service
public class SessionAttendanceServiceImpl extends ServiceImpl<SessionAttendanceMapper, SessionAttendance>
        implements SessionAttendanceService{


    // 学生加入直播
    @Override
    public SessionAttendance joinSession(int sessionId, int studentId) {
        // 检查是否已存在未结束的记录
        LambdaQueryWrapper<SessionAttendance> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SessionAttendance::getSessionId, sessionId)
                .eq(SessionAttendance::getStudentId, studentId)
                .isNull(SessionAttendance::getLeaveTime); // 只查询未离开的记录

        if (this.count(queryWrapper) > 0) {
            throw new IllegalStateException("学生已加入直播且未离开");
        }

        SessionAttendance attendance = new SessionAttendance();
        attendance.setSessionId(sessionId);
        attendance.setStudentId(studentId);
        attendance.setJoinTime(new Date());
        attendance.setInteractionCount(0); // 初始化互动次数

        this.save(attendance);
        return attendance;
    }

    // 学生离开直播
    @Override
    public SessionAttendance leaveSession(int sessionId, int studentId) {
        LambdaQueryWrapper<SessionAttendance> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SessionAttendance::getSessionId, sessionId)
                .eq(SessionAttendance::getStudentId, studentId)
                .isNull(SessionAttendance::getLeaveTime); // 只查询未离开的记录

        SessionAttendance attendance = this.getOne(queryWrapper);
        if (attendance == null) {
            throw new ResourceNotFoundException("未找到有效的参与记录");
        }

        Date now = new Date();
        attendance.setLeaveTime(now);

        // 计算参与时长（秒）
        Duration duration = Duration.between(
                attendance.getJoinTime().toInstant(),
                now.toInstant()
        );
        attendance.setDuration((int) duration.getSeconds());

        this.updateById(attendance);
        return attendance;
    }

    // 更新互动次数
    @Override
    public void incrementInteractionCount(int sessionId, int studentId) {
        LambdaQueryWrapper<SessionAttendance> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SessionAttendance::getSessionId, sessionId)
                .eq(SessionAttendance::getStudentId, studentId)
                .isNull(SessionAttendance::getLeaveTime); // 只更新未离开的记录

        SessionAttendance attendance = this.getOne(queryWrapper);
        if (attendance == null) {
            throw new ResourceNotFoundException("参与记录不存在或学生已离开");
        }

        // 处理可能的null值
        Integer currentCount = attendance.getInteractionCount();
        attendance.setInteractionCount(currentCount != null ? currentCount + 1 : 1);

        this.updateById(attendance);
    }

}