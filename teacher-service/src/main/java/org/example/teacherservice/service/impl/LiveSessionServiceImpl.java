package org.example.teacherservice.service.impl;


import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.teacherservice.dto.TrtcParams;
import org.example.teacherservice.entity.LiveSession;
import org.example.teacherservice.exception.ResourceNotFoundException;
import org.example.teacherservice.mapper.LiveSessionMapper;
import org.example.teacherservice.service.LiveSessionService;
import org.example.teacherservice.service.TrtcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LiveSessionServiceImpl extends ServiceImpl<LiveSessionMapper, LiveSession> implements LiveSessionService {


    @Autowired
    private TrtcService trtcService;

    // 创建直播会话
    @Override
    public LiveSession createLiveSession(LiveSession liveSession) {
        liveSession.setSessionTitle(liveSession.getSessionTitle());
        liveSession.setMaxUsers(liveSession.getMaxUsers());
        System.out.println("进入直播创建的服务"+liveSession.getSessionTitle());
        // 生成TRTC房间ID
        String roomId = trtcService.generateRoomId();
        System.out.println("roomId = " + roomId);
        liveSession.setTrtcRoomId(roomId);
        liveSession.setStatus("scheduled");
        System.out.println("liveSession = " + liveSession.getCreatedAt());

        // 使用MyBatis-Plus的保存方法
        try {
            this.save(liveSession);
        } catch (Exception e) {
            e.printStackTrace(); // 添加这行打印异常
            throw e; // 重新抛出或处理异常
        }
        return liveSession;
    }

    // 开始直播
    @Override
    public LiveSession startLiveSession(int sessionId) {
        LiveSession session = this.getById(sessionId);
        if (session == null) {
            throw new ResourceNotFoundException("直播会话不存在");
        }

        if (!"scheduled".equals(session.getStatus())) {
            throw new IllegalStateException("直播状态不正确");
        }

        session.setStartTime(new Date());
        session.setStatus("live");
        this.updateById(session);
        return session;
    }

    // 结束直播
    @Override
    public LiveSession endLiveSession(int sessionId) {
        LiveSession session = this.getById(sessionId);
        if (session == null) {
            throw new ResourceNotFoundException("直播会话不存在");
        }

        if (!"live".equals(session.getStatus())) {
            throw new IllegalStateException("直播状态不正确");
        }

        session.setStatus("ended");
        session.setEndTime(new Date());
        this.updateById(session);
        return session;
    }

    // 获取TRTC参数
    @Override
    public TrtcParams getTrtcParamsForSession(int sessionId, int userId, String role) {
        LiveSession session = this.getById(sessionId);
        if (session == null) {
            throw new ResourceNotFoundException("直播会话不存在");
        }

        if (!"live".equals(session.getStatus())) {
            throw new IllegalStateException("直播未进行中");
        }
        // 用户ID格式：角色_用户ID
        String trtcUserId = role + "_" + userId;
        return trtcService.getTrtcParams(session, trtcUserId);
    }

    @Override
    public TrtcParams getTrtcParamsForSession_1(int userId, String role) {

        // 用户ID格式：角色_用户ID
        String trtcUserId = role + "_" + userId;
        return trtcService.getTrtcParams_1(trtcUserId);
    }


}