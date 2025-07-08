package org.example.teacherauth.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.teacherauth.entity.User;

import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT u.*, d.department_name FROM users u LEFT JOIN departments d ON u.department_id = d.department_id WHERE u.username = #{username}")
    User findByUsername(@Param("username") String username);
}