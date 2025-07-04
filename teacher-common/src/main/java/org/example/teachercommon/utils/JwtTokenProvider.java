package org.example.teachercommon.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.teachercommon.entity.JwtUserInfo;
import org.example.teachercommon.entity.UserType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final SecretKey secretKey;
    private final long expirationMs;


    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationMs) {
        // Base64 解码为字节数组
        byte[] keyBytes = Decoders.BASE64.decode(secret);  // 使用jjwt提供的解码器
        // 验证密钥长度（必须 256-bit = 32字节）
        if (keyBytes.length != 32) {
            throw new IllegalArgumentException("JWT 密钥必须为 32 字节（256-bit）的 Base64 字符串");
        }
        // 将字符串密钥转换为安全的HMAC-SHA密钥
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.expirationMs = expirationMs;
    }

    /**
     * 生成JWT令牌
     * @param userId 用户ID
     * @param username 用户名
     * @param userType 用户类型
     * @return 签名的JWT令牌字符串
     */
    public String generateToken(Integer userId, String username, UserType userType) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("userType", userType.name())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析令牌为完整用户信息
     * @param token JWT令牌
     * @return 包含用户信息的DTO对象
     * @throws JwtException 如果令牌无效或过期
     */
    public JwtUserInfo parseTokenToUserInfo(String token) throws JwtException {
        Claims claims = parseTokenClaims(token);
        return JwtUserInfo.builder()
                .userId(claims.get("userId", Integer.class))
                .username(claims.getSubject())
                .userType(claims.get("userType", String.class))
                .issuedAt(claims.getIssuedAt())
                .expiresAt(claims.getExpiration())
                .build();
    }

    /**
     * 验证令牌有效性
     * @param token JWT令牌
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            parseTokenClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // ---------- 快捷方法 ----------
    public Integer getUserIdFromToken(String token) {
        return parseTokenClaims(token).get("userId", Integer.class);
    }

    public String getUsernameFromToken(String token) {
        return parseTokenClaims(token).getSubject();
    }

    public String getUserTypeFromToken(String token) {
        return parseTokenClaims(token).get("userType", String.class);
    }

    // ---------- 私有方法 ----------
    private Claims parseTokenClaims(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}