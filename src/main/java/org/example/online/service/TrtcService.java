package org.example.online.service;


import org.example.online.dto.TrtcParams;
import org.example.online.entity.LiveSession;

public interface TrtcService {
    String generateUserSig(String userId);

    String generateRoomId();

    TrtcParams getTrtcParams(LiveSession session,String userId);
}
