package org.example.teacherauth.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.teacherauth.entity.Department;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DepartmentMapper extends BaseMapper<Department> {
    @Select("SELECT name FROM departments WHERE department_id = #{departmentId}")
    String selectDepartmentNameById(@Param("departmentId") Integer departmentId);
}
