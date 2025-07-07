package org.example.teacherservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.example.teacherservice.entity.AssignmentClasses;
import org.example.teacherservice.vo.ClassSimpleVO;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface AssignmentClassesMapper extends BaseMapper<AssignmentClasses> {
    @Delete("DELETE FROM assignment_classes WHERE assignment_id = #{assignmentId}")
    int deleteByAssignmentId(@Param("assignmentId") Integer assignmentId);

    @Select("SELECT class_id FROM assignment_classes WHERE assignment_id = #{assignmentId} AND class_id IN (#{classIds})")
    List<Integer> selectExistingClassIds(@Param("assignmentId") Integer assignmentId,
                                         @Param("classIds") List<Integer> classIds);

    @Insert("<script>" +
            "INSERT INTO assignment_classes (assignment_id, class_id, deadline, publish_status) " +
            "VALUES " +
            "<foreach collection='classIds' item='classId' separator=','>" +
            "(#{assignmentId}, #{classId}, #{deadline}, #{publishStatus})" +
            "</foreach>" +
            "</script>")
    int batchInsert(@Param("assignmentId") Integer assignmentId,
                    @Param("classIds") List<Integer> classIds,
                    @Param("deadline") Date deadline,
                    @Param("publishStatus") Integer publishStatus);

    @Delete("DELETE FROM assignment_classes WHERE assignment_id = #{assignmentId} AND class_id = #{classId}")
    int deleteByAssignmentAndClass(@Param("assignmentId") Integer assignmentId,
                                   @Param("classId") Integer classId);

    @Select("SELECT c.class_id, c.class_name FROM classes c " +
            "JOIN assignment_classes ac ON c.class_id = ac.class_id " +
            "WHERE ac.assignment_id = #{assignmentId}")
    List<ClassSimpleVO> selectClassesByAssignmentId(@Param("assignmentId") Integer assignmentId);

    @Select("<script>" +
            "SELECT a.assignment_id, c.class_name FROM assignment_classes a " +
            "JOIN classes c ON a.class_id = c.class_id " +
            "WHERE a.assignment_id IN " +
            "<foreach collection='assignmentIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<Map<String, Object>> selectClassNamesByAssignmentIds(@Param("assignmentIds") List<Integer> assignmentIds);
}
