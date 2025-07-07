package org.example.online.dto;

public record TrtcParams(
        Long sdkAppId,
        String userId,
        String roomId,
        String userSig
) {}