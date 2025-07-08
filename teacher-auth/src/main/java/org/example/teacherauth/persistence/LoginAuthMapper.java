package org.example.teacherauth.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.teacherauth.entity.LoginAuth;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface LoginAuthMapper extends BaseMapper<LoginAuth> {
    @Select("SELECT password_hash FROM login_auth WHERE user_id = #{userId}")
    Optional<String> selectPasswordByUserId(Integer userId); // 返回Optional
    
    @Select("SELECT * FROM login_auth la JOIN users u ON la.user_id = u.user_id WHERE u.username = #{username}")
    LoginAuth findByUsername(@Param("username") String username);
}
