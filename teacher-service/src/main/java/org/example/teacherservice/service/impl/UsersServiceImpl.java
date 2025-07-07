package org.example.teacherservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.teacherservice.entity.Users;
import org.example.teacherservice.mapper.UsersMapper;
import org.example.teacherservice.service.UsersService;
import org.springframework.stereotype.Service;

/**
 * @author tang_
 * @description 针对表【users(用户基本信息表)】的数据库操作Service实现
 * @createDate 2025-07-05 11:32:10
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
        implements UsersService {

}
