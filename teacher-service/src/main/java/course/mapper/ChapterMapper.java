package course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import course.entity.Chapter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface ChapterMapper extends BaseMapper<Chapter> {
    @Select("SELECT MAX(order_num) FROM chapters WHERE course_id = #{courseId} AND parent_id <=> #{parentId}")
    Integer findMaxOrderNumByParent(@Param("courseId") Integer courseId, @Param("parentId") Integer parentId);

    @Update("UPDATE chapters SET order_num = order_num - 1 WHERE parent_id <=> #{parentId} AND order_num > #{deletedOrderNum}")
    void reorderChaptersAfterDeletion(@Param("parentId") Integer parentId, @Param("deletedOrderNum") Integer deletedOrderNum);

    @Select("<script>" +
            "SELECT course_id, COUNT(*) as chapter_count FROM chapters " +
            "WHERE course_id IN " +
            "<foreach item='item' index='index' collection='courseIds' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "GROUP BY course_id" +
            "</script>")
    List<Map<String, Object>> countChaptersByCourseIds(@Param("courseIds") List<Integer> courseIds);
} 