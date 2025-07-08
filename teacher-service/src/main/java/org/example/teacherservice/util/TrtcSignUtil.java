package org.example.teacherservice.util;

import com.tencentyun.TLSSigAPIv2;



public class TrtcSignUtil {

    public static String generateRoomLevelToken(
            long sdkAppId,
            String secretKey,
            String userId,
            int expire
    ) {
        TLSSigAPIv2 api = new TLSSigAPIv2(sdkAppId, secretKey);
        return api.genUserSig(userId, expire);
    }
}