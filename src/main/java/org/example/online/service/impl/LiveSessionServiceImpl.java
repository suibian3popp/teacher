package org.example.online.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.online.dto.TrtcParams;
import org.example.online.entity.LiveSession;
import org.example.online.exception.ResourceNotFoundException;
import org.example.online.mapper.LiveSessionMapper;
import org.example.online.service.LiveSessionService;
import org.example.online.service.TrtcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author lenovo
* @description 针对表【live_session(直播课程表（由教师创建）)】的数据库操作Service实现
* @createDate 2025-06-29 20:52:01
*/
@Service
public class LiveSessionServiceImpl extends ServiceImpl<LiveSessionMapper, LiveSession> implements LiveSessionService{

    @Autowired
    private TrtcService trtcService;

    // 创建直播会话
    @Override
    public LiveSession createLiveSession(LiveSession liveSession) {
        // 生成TRTC房间ID
        String roomId = trtcService.generateRoomId();
        liveSession.setTrtcRoomId(roomId);
        liveSession.setStatus("scheduled");

        // 使用MyBatis-Plus的保存方法
        this.save(liveSession);
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
        return trtcService.getTrtcParams(session,trtcUserId);
    }
}



