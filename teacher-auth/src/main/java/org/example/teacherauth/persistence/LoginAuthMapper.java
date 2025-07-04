package org.example.teacherauth.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.teacherauth.entity.LoginAuth;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface LoginAuthMapper extends BaseMapper<LoginAuth> {
    @Select("SELECT password_hash FROM login_auth WHERE user_id = #{userId}")
    Optional<String> selectPasswordByUserId(Integer userId); // 返回Optional
}
