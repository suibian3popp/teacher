package org.example.teacherservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ConfigLoadTest {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    private Environment env;

    @Test
    void shouldLoadJwtSecret() {
        // 验证配置是否加载
        assertThat(jwtSecret).isNotNull();

        // 调试输出
        System.out.println("JWT Secret from @Value: " + jwtSecret);
        System.out.println("JWT Secret from Environment: " + env.getProperty("jwt.secret"));
    }
}