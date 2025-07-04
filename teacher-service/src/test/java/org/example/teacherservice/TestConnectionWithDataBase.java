package org.example.teacherservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TestConnectionWithDataBase {
    @Autowired
    private DataSource dataSource;

    // 测试用数据库配置（硬编码）
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/teacher?useSSL=true&characterEncoding=UTF8&serverTimezone=GMT";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "@Lluo1314520"; // 替换为你的真实密码

    // 动态覆盖Spring的数据库配置
    @DynamicPropertySource
    static void overrideDatabaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> DB_URL);
        registry.add("spring.datasource.username", () -> DB_USERNAME);
        registry.add("spring.datasource.password", () -> DB_PASSWORD);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
    }

    @Test
    void testConnection() throws SQLException {
        // 直接从数据源获取连接
        try (Connection conn = dataSource.getConnection()) {
            assertNotNull(conn, "数据库连接不应为null");
            System.out.println("=== 数据库连接测试成功 ===");
            System.out.println("URL: " + conn.getMetaData().getURL());
            System.out.println("数据库产品: " + conn.getMetaData().getDatabaseProductName());
            System.out.println("版本: " + conn.getMetaData().getDatabaseProductVersion());
        }
    }

}





