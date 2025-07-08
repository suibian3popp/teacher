package org.example.teacherservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.example.teacherservice.dto.UserDTO;
import org.example.teacherservice.entity.User;
import org.example.teacherservice.mapper.UserMapper;
import org.example.teacherservice.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public List<UserDTO> getAssistants() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", "ta");
        List<User> users = userMapper.selectList(queryWrapper);

        return users.stream()
                .map(user -> UserDTO.builder()
                        .userId(user.getUserId())
                        .realName(user.getRealName())
                        .build())
                .collect(Collectors.toList());
    }
} 