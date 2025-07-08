package org.example.teacherservice.configs;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "trtc")
public class TrtcConfig {


    private Long sdkAppId;
    //    private String secretId;     // 新增腾讯云 Secret ID
    private String secretKey;    // 腾讯云 Secret Key
    private int expireTime = 180; // 默认180秒

    public void setSdkAppId(Long sdkAppId) {
        this.sdkAppId = sdkAppId;
    }

    public Long getSdkAppId() {
        return sdkAppId;
    }

//    public String getSecretId() { return secretId; }
//    public void setSecretId(String secretId) { this.secretId = secretId; }

    public String getSecretKey() { return secretKey; }
    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }

    public int getExpireTime() { return expireTime; }
    public void setExpireTime(int expireTime) { this.expireTime = expireTime; }
}