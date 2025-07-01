package org.demo_hd.mapper;

import org.demo_hd.entity.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author tang_
* @description 针对表【t_user_info】的数据库操作Mapper
* @createDate 2025-06-28 19:38:31
* @Entity org.example.entity.UserInfo
*/
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    UserInfo selectUserById(Integer id);

    List<UserInfo> selectUserList();
}




