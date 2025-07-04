package org.example.teachercommon;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
})
public class JwtConfigTester {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Test
    public void init() {
        System.out.println("✅ JWT Secret from config: " + secret);
        System.out.println("✅ JWT Expiration from config: " + expiration);
    }
}