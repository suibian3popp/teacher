package org.example.online.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.online.dto.TrtcParams;
import org.example.online.entity.LiveSession;

/**
* @author lenovo
* @description 针对表【live_session(直播课程表（由教师创建）)】的数据库操作Service
* @createDate 2025-06-29 20:52:01
*/
public interface LiveSessionService extends IService<LiveSession> {
     LiveSession createLiveSession(LiveSession liveSession);

     LiveSession startLiveSession(int sessionId);

     LiveSession endLiveSession(int sessionId);

     TrtcParams getTrtcParamsForSession(int sessionId, int userId, String role);

}
