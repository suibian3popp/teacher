package course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import course.entity.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {

    @Select("""
            <script>
            SELECT r.* FROM resources r
            JOIN chapter_resources cr ON r.resource_id = cr.resource_id
            WHERE cr.chapter_id IN
            <foreach item='item' index='index' collection='chapterIds' open='(' separator=',' close=')'>
                #{item}
            </foreach>
            </script>
            """)
    List<Resource> findResourcesByChapterIds(@Param("chapterIds") List<Integer> chapterIds);
} 