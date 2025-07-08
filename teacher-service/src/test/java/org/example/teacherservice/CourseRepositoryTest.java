package org.example.teacherservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class CourseRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testCourseTableStructure() {
        // 检查课程表结构
        List<Map<String, Object>> columns = jdbcTemplate.queryForList(
                "SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_KEY " +
                        "FROM INFORMATION_SCHEMA.COLUMNS " +
                        "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'course' " +
                        "ORDER BY ORDINAL_POSITION");

        assertFalse(columns.isEmpty(), "课程表应该有列");
        System.out.println("课程表结构:");

        for (Map<String, Object> column : columns) {
            System.out.println(column.get("COLUMN_NAME") + " - " +
                    column.get("DATA_TYPE") + " - " +
                    column.get("IS_NULLABLE") + " - " +
                    column.get("COLUMN_KEY"));

            // 检查关键字段
            if ("course_id".equals(column.get("COLUMN_NAME"))) {
                assertEquals("PRI", column.get("COLUMN_KEY"), "course_id应该是主键");
            }
        }
    }

    @Test
    public void testCourseInsertAndSelect() {
        // 测试课程添加和查询
        try {
            // 生成测试课程ID（使用时间戳避免冲突）
            String testCourseName = "测试课程-" + System.currentTimeMillis();
            LocalDateTime now = LocalDateTime.now();

            // 插入测试课程
            int rowsAffected = jdbcTemplate.update(
                    "INSERT INTO course (course_name, course_type, teacher_id, credit, create_time) VALUES (?, ?, ?, ?, ?)",
                    testCourseName, "recorded", 1, 3.0, now);

            assertEquals(1, rowsAffected, "应成功插入一条课程记录");

            // 查询刚插入的课程
            Map<String, Object> course = jdbcTemplate.queryForMap(
                    "SELECT * FROM course WHERE course_name = ?", testCourseName);

            assertNotNull(course, "应能查询到插入的课程");
            assertEquals(testCourseName, course.get("course_name"), "课程名称应匹配");
            assertEquals("recorded", course.get("course_type"), "课程类型应匹配");

            // 清理测试数据
            jdbcTemplate.update("DELETE FROM course WHERE course_name = ?", testCourseName);

            System.out.println("课程表测试完成：插入和查询均正常");

        } catch (Exception e) {
            fail("课程表操作测试失败: " + e.getMessage());
        }
    }
}