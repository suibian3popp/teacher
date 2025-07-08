package org.example.teacherservice.service;


import com.baomidou.mybatisplus.extension.service.IService;

import org.example.teacherservice.dto.TrtcParams;
import org.example.teacherservice.entity.LiveSession;

public interface LiveSessionService extends IService<LiveSession>{

    LiveSession createLiveSession(LiveSession liveSession);

    LiveSession startLiveSession(int sessionId);

    LiveSession endLiveSession(int sessionId);

    TrtcParams getTrtcParamsForSession(int sessionId, int userId, String role);
}
