package org.example.teacherauth;

import org.example.teacherauth.configs.JwtFilterConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

//@SpringBootTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(JwtFilterConfig.class) // 明确导入过滤器配置
class TeacherAuthApplicationTests {

    @Test
    void contextLoads() {
    }

}
