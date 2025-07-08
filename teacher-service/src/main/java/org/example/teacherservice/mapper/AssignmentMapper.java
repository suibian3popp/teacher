package org.example.teacherservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.teacherservice.entity.Assignment;
import org.example.teacherservice.vo.assignment.AssignmentSearchResult;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AssignmentMapper extends BaseMapper<Assignment> {
    @Select("SELECT COUNT(*) FROM assignments WHERE creator_id = #{creatorId}")
    Long countByCreatorId(@Param("creatorId") Integer creatorId);

    @Select("SELECT * FROM assignments WHERE creator_id = #{creatorId} ORDER BY create_time DESC LIMIT #{offset}, #{pageSize}")
    List<Assignment> selectByCreatorId(@Param("creatorId") Integer creatorId,
                                       @Param("offset") Integer offset,
                                       @Param("pageSize") Integer pageSize);

    @Select("SELECT * FROM assignments WHERE resource_id = #{resourceId}")
    List<Assignment> selectByResourceId(@Param("resourceId") Integer resourceId);

    @Select("<script>" +
            "SELECT a.* FROM assignments a " +
            "WHERE 1=1 " +
            "<if test='titleKeyword != null and titleKeyword != \"\"'>" +
            "AND a.title LIKE CONCAT('%', #{titleKeyword}, '%') " +
            "</if>" +
            "<if test='creatorId != null'>" +
            "AND a.creator_id = #{creatorId} " +
            "</if>" +
            "<if test='status != null'>" +
            "AND (CASE " +
            "WHEN #{status} = 1 THEN a.deadline &gt; NOW() " +  // 转义 >
            "WHEN #{status} = 2 THEN a.deadline = NOW() " +
            "WHEN #{status} = 3 THEN a.deadline &lt; NOW() " +  // 转义 <
            "END) " +
            "</if>" +
            "ORDER BY a.create_time DESC" +
            "</script>")
    List<AssignmentSearchResult> searchAssignments(@Param("titleKeyword") String titleKeyword,
                                                   @Param("creatorId") Integer creatorId,
                                                   @Param("status") Integer status);
}
