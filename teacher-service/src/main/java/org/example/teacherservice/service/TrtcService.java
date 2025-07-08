package org.example.teacherservice.service;

import org.example.teacherservice.dto.TrtcParams;
import org.example.teacherservice.entity.LiveSession;

public interface TrtcService {
    String generateUserSig(String userId);

    String generateRoomId();

    TrtcParams getTrtcParams(LiveSession session,String userId);
}