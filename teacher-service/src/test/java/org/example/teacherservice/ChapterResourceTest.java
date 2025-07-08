package org.example.teacherservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ChapterResourceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Test
    public void testChapterResourceTableStructure() {
        // 检查章节资源关联表结构
        List<Map<String, Object>> columns = jdbcTemplate.queryForList(
                "SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_KEY " +
                "FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'chapter_resource' " +
                "ORDER BY ORDINAL_POSITION");
        
        assertFalse(columns.isEmpty(), "章节资源关联表应该有列");
        System.out.println("章节资源关联表结构:");
        
        for (Map<String, Object> column : columns) {
            System.out.println(column.get("COLUMN_NAME") + " - " + 
                               column.get("DATA_TYPE") + " - " + 
                               column.get("IS_NULLABLE") + " - " + 
                               column.get("COLUMN_KEY"));
        }
    }
    
    @Test
    public void testLeafNodeResourceAssociation() {
        try {
            // 创建测试课程
            String testCourseName = "测试资源课程-" + System.currentTimeMillis();
            jdbcTemplate.update(
                "INSERT INTO course (course_name, course_type, teacher_id, credit, create_time) VALUES (?, ?, ?, ?, NOW())",
                testCourseName, "recorded", 1, 3.0);
            
            // 获取刚插入的课程ID
            Integer courseId = jdbcTemplate.queryForObject(
                    "SELECT course_id FROM course WHERE course_name = ?", 
                    Integer.class, 
                    testCourseName);
            
            // 插入叶子节点章节
            jdbcTemplate.update(
                "INSERT INTO chapter (course_id, parent_id, chapter_name, sort_order, is_leaf) VALUES (?, ?, ?, ?, ?)",
                courseId, 0, "资源章节", 1, true);
            
            Integer chapterId = jdbcTemplate.queryForObject(
                    "SELECT chapter_id FROM chapter WHERE course_id = ? AND chapter_name = ?", 
                    Integer.class, 
                    courseId, "资源章节");
            
            // 插入测试资源
            jdbcTemplate.update(
                "INSERT INTO resource (res_name, res_type, uploader_id, file_path, file_size, subject) VALUES (?, ?, ?, ?, ?, ?)",
                "测试资源", "pdf", 1, "/test/path.pdf", 1024, "测试学科");
            
            Integer resId = jdbcTemplate.queryForObject(
                    "SELECT res_id FROM resource WHERE res_name = ?", 
                    Integer.class, 
                    "测试资源");
            
            // 关联章节和资源
            jdbcTemplate.update(
                "INSERT INTO chapter_resource (chapter_id, res_id, sort_order) VALUES (?, ?, ?)",
                chapterId, resId, 1);
            
            // 验证关联关系
            Map<String, Object> association = jdbcTemplate.queryForMap(
                    "SELECT * FROM chapter_resource WHERE chapter_id = ? AND res_id = ?", 
                    chapterId, resId);
            
            assertNotNull(association, "应该存在章节资源关联");
            assertEquals(chapterId, association.get("chapter_id"));
            assertEquals(resId, association.get("res_id"));
            
            // 清理测试数据
            jdbcTemplate.update("DELETE FROM chapter_resource WHERE chapter_id = ? AND res_id = ?", chapterId, resId);
            jdbcTemplate.update("DELETE FROM chapter WHERE chapter_id = ?", chapterId);
            jdbcTemplate.update("DELETE FROM resource WHERE res_id = ?", resId);
            jdbcTemplate.update("DELETE FROM course WHERE course_id = ?", courseId);
            
            System.out.println("章节资源关联测试完成");
            
        } catch (Exception e) {
            fail("章节资源关联测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Test
    public void testTriggerForLeafNodeOnly() {
        // 测试触发器是否正确阻止非叶子节点关联资源
        try {
            // 创建测试课程
            String testCourseName = "测试触发器课程-" + System.currentTimeMillis();
            jdbcTemplate.update(
                "INSERT INTO course (course_name, course_type, teacher_id, credit, create_time) VALUES (?, ?, ?, ?, NOW())",
                testCourseName, "recorded", 1, 3.0);
            
            Integer courseId = jdbcTemplate.queryForObject(
                    "SELECT course_id FROM course WHERE course_name = ?", 
                    Integer.class, 
                    testCourseName);
            
            // 插入非叶子节点章节
            jdbcTemplate.update(
                "INSERT INTO chapter (course_id, parent_id, chapter_name, sort_order, is_leaf) VALUES (?, ?, ?, ?, ?)",
                courseId, 0, "父章节", 1, false);
            
            Integer parentChapterId = jdbcTemplate.queryForObject(
                    "SELECT chapter_id FROM chapter WHERE course_id = ? AND chapter_name = ?", 
                    Integer.class, 
                    courseId, "父章节");
            
            // 插入测试资源
            jdbcTemplate.update(
                "INSERT INTO resource (res_name, res_type, uploader_id, file_path, file_size, subject) VALUES (?, ?, ?, ?, ?, ?)",
                "测试触发器资源", "pdf", 1, "/test/trigger.pdf", 1024, "测试学科");
            
            Integer resId = jdbcTemplate.queryForObject(
                    "SELECT res_id FROM resource WHERE res_name = ?", 
                    Integer.class, 
                    "测试触发器资源");
            
            // 尝试关联非叶子节点和资源，应该失败
            boolean triggerWorked = false;
            try {
                jdbcTemplate.update(
                    "INSERT INTO chapter_resource (chapter_id, res_id, sort_order) VALUES (?, ?, ?)",
                    parentChapterId, resId, 1);
            } catch (Exception e) {
                // 触发器应该抛出异常
                triggerWorked = true;
                System.out.println("触发器正确阻止了非叶子节点关联资源");
            }
            
            assertTrue(triggerWorked, "触发器应该阻止非叶子节点关联资源");
            
            // 清理测试数据
            jdbcTemplate.update("DELETE FROM chapter WHERE chapter_id = ?", parentChapterId);
            jdbcTemplate.update("DELETE FROM resource WHERE res_id = ?", resId);
            jdbcTemplate.update("DELETE FROM course WHERE course_id = ?", courseId);
            
        } catch (Exception e) {
            fail("触发器测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 