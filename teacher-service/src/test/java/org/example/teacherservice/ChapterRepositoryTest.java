package org.example.teacherservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class ChapterRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testChapterTableStructure() {
        // 检查章节表结构
        List<Map<String, Object>> columns = jdbcTemplate.queryForList(
                "SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_KEY " +
                        "FROM INFORMATION_SCHEMA.COLUMNS " +
                        "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'chapter' " +
                        "ORDER BY ORDINAL_POSITION");

        assertFalse(columns.isEmpty(), "章节表应该有列");
        System.out.println("章节表结构:");

        boolean hasIsLeafColumn = false;

        for (Map<String, Object> column : columns) {
            String columnName = (String) column.get("COLUMN_NAME");
            System.out.println(columnName + " - " +
                    column.get("DATA_TYPE") + " - " +
                    column.get("IS_NULLABLE") + " - " +
                    column.get("COLUMN_KEY"));

            // 检查关键字段
            if ("chapter_id".equals(columnName)) {
                assertEquals("PRI", column.get("COLUMN_KEY"), "chapter_id应该是主键");
            }

            if ("is_leaf".equals(columnName)) {
                hasIsLeafColumn = true;
            }
        }

        assertTrue(hasIsLeafColumn, "章节表应该有is_leaf列");
    }

    @Test
    @Transactional
    public void testChapterHierarchy() {
        try {


            // 创建测试课程，使用上面创建的用户ID作为teacher_id
            String testCourseName = "测试章节课程-" + System.currentTimeMillis();
            jdbcTemplate.update(
                    "INSERT INTO course (course_name, course_type, teacher_id, credit, create_time) VALUES (?, ?, ?, ?, NOW())",
                    testCourseName, "recorded", 999, 3.0);

            // 获取刚插入的课程ID
            Integer courseId = jdbcTemplate.queryForObject(
                    "SELECT course_id FROM course WHERE course_name = ?",
                    Integer.class,
                    testCourseName);

            assertNotNull(courseId, "课程ID不应为空");

            // 插入顶级章节
            jdbcTemplate.update(
                    "INSERT INTO chapter (course_id, parent_id, chapter_name, sort_order, is_leaf) VALUES (?, ?, ?, ?, ?)",
                    courseId, 0, "第一章", 1, false);

            // 获取顶级章节ID
            Integer parentChapterId = jdbcTemplate.queryForObject(
                    "SELECT chapter_id FROM chapter WHERE course_id = ? AND chapter_name = ?",
                    Integer.class,
                    courseId, "第一章");

            assertNotNull(parentChapterId, "顶级章节ID不应为空");

            // 插入子章节
            jdbcTemplate.update(
                    "INSERT INTO chapter (course_id, parent_id, chapter_name, sort_order, is_leaf) VALUES (?, ?, ?, ?, ?)",
                    courseId, parentChapterId, "1.1节", 1, true);

            // 验证章节关系
            Integer childCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM chapter WHERE parent_id = ?",
                    Integer.class,
                    parentChapterId);

            assertEquals(1, childCount, "应该有一个子章节");

            // 清理测试数据
            //jdbcTemplate.update("DELETE FROM chapter WHERE course_id = ?", courseId);
            //jdbcTemplate.update("DELETE FROM course WHERE course_id = ?", courseId);
            //jdbcTemplate.update("DELETE FROM user WHERE user_id = ?", 999);

            System.out.println("章节表层级关系测试完成");


        } catch (Exception e) {

            fail("章节表测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
