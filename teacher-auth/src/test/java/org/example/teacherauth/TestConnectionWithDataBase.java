package org.example.teacherauth;

import org.example.teacherauth.configs.JwtFilterConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@PropertySource("classpath:application.properties")
@SpringBootTest(
        classes = TeacherAuthApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "debug=true",
                "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration," +
                        "org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration",
        }
)
@Import(JwtFilterConfig.class) // 明确导入过滤器配置
public class TestConnectionWithDataBase {

    @Autowired
    private DataSource dataSource;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Test
    void testConnection() throws SQLException {
        System.out.println("spring.datasource.url: " + dbUrl);
        try (Connection conn = dataSource.getConnection()) {
            assertNotNull(conn);
            System.out.println("数据库连接成功：" + conn.getMetaData().getURL());
        }
    }

    @Test
    void pureJunitTest() {
        System.out.println("==== 这个输出能看见吗？ ====");
        // 简单断言，确保测试执行
        assertNotNull(dataSource);
    }
}