package org.example.teacherservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DatabaseConnectionTest {

    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Test
    public void testDataSourceConnection() throws SQLException {
        // 测试数据源连接
        assertNotNull(dataSource, "数据源不应为空");
        
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            assertTrue(connection.isValid(1), "数据库连接应该有效");
            assertFalse(connection.isClosed(), "数据库连接不应关闭");
            
            System.out.println("数据库连接成功!");
            System.out.println("数据库URL: " + connection.getMetaData().getURL());
            System.out.println("数据库产品名称: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("数据库产品版本: " + connection.getMetaData().getDatabaseProductVersion());
            System.out.println("数据库驱动名称: " + connection.getMetaData().getDriverName());
            System.out.println("数据库驱动版本: " + connection.getMetaData().getDriverVersion());
        } finally {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }
    
    @Test
    public void testJdbcTemplateQuery() {
        // 测试简单查询
        assertNotNull(jdbcTemplate, "JdbcTemplate不应为空");
        
        try {
            // 查询数据库版本
            String dbVersion = jdbcTemplate.queryForObject("SELECT VERSION()", String.class);
            assertNotNull(dbVersion, "数据库版本不应为空");
            System.out.println("数据库版本: " + dbVersion);
            
            // 测试查询用户表是否存在
            Integer tableCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'user'", 
                    Integer.class);
            assertNotNull(tableCount, "表计数不应为空");
            System.out.println("用户表存在: " + (tableCount > 0));
            
            // 测试查询课程表是否存在
            tableCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'course'", 
                    Integer.class);
            assertNotNull(tableCount, "表计数不应为空");
            System.out.println("课程表存在: " + (tableCount > 0));
        } catch (Exception e) {
            fail("执行数据库查询时出错: " + e.getMessage());
        }
    }
} 