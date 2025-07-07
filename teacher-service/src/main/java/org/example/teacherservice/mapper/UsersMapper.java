package org.example.teacherservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.teacherservice.entity.Users;
import org.springframework.stereotype.Repository;

/**
 * @author tang_
 * @description 针对表【users(用户基本信息表)】的数据库操作Mapper
 * @createDate 2025-07-05 11:32:10
 * @Entity org.demo_hd.entity.Users
 */
@Mapper
@Repository
public interface UsersMapper extends BaseMapper<Users> {

}