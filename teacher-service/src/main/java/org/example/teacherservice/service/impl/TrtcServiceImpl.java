package org.example.teacherservice.service.impl;

import com.tencentyun.TLSSigAPIv2;

import org.example.teacherservice.configs.TrtcConfig;
import org.example.teacherservice.dto.TrtcParams;
import org.example.teacherservice.entity.LiveSession;
import org.example.teacherservice.service.TrtcService;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class TrtcServiceImpl implements TrtcService {

    private final TrtcConfig trtcConfig;
    private final TLSSigAPIv2 tlsSigApi;

    private String roomId;

    public TrtcServiceImpl(TrtcConfig trtcConfig) {
        this.trtcConfig = trtcConfig;
        // 初始化腾讯云签名工具
        this.tlsSigApi = new TLSSigAPIv2(trtcConfig.getSdkAppId(), trtcConfig.getSecretKey());
    }


    @Override
    public String generateUserSig(String userId) {
        // 使用腾讯云官方SDK生成签名
        return tlsSigApi.genUserSig(userId, trtcConfig.getExpireTime());
    }


    @Override
    public String generateRoomId() {
        // 使用UUID确保全局唯一性
        return "live_" + UUID.randomUUID().toString().replace("-", "");
    }


    @Override
    public TrtcParams getTrtcParams(LiveSession session, String userId) {
        return new TrtcParams(
                trtcConfig.getSdkAppId(),
                userId,
                session.getTrtcRoomId(),
                generateUserSig(userId)
        );
    }

    @Override
    public TrtcParams getTrtcParams_1(String userId) {
        return new TrtcParams(
                trtcConfig.getSdkAppId(),
                userId,
                roomId,
                generateUserSig(userId)
        );
    }

}