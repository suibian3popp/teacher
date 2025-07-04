//package org.example.teachercommon.utils;
//
//import io.jsonwebtoken.security.Keys;
//import io.jsonwebtoken.io.Encoders;
//import javax.crypto.SecretKey;
//import io.jsonwebtoken.security.Keys;
//import io.jsonwebtoken.io.Encoders;
//
//public class JwtKeyGenerator {
//    public static void main(String[] args) {
//
//        String key = Encoders.BASE64.encode(
//                Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256).getEncoded()
//        );
//        System.out.println("安全密钥: " + key);
//
//    }
//}
//一次性用品