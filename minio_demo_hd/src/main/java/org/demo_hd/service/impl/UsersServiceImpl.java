package org.demo_hd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.demo_hd.entity.Users;
import org.demo_hd.service.UsersService;
import org.demo_hd.mapper.UsersMapper;
import org.springframework.stereotype.Service;

/**
* @author tang_
* @description 针对表【users(用户基本信息表)】的数据库操作Service实现
* @createDate 2025-07-01 13:28:38
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{

}




