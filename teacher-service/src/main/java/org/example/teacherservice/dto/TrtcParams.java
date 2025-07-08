package org.example.teacherservice.dto;

public record TrtcParams(
        Long sdkAppId,
        String userId,
        String roomId,
        String userSig
) {}