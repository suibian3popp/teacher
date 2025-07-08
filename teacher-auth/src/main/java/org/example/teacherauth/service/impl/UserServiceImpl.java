package org.example.teacherauth.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.example.teacherauth.dto.UserCreateDTO;
import org.example.teacherauth.entity.LoginAuth;
import org.example.teacherauth.entity.User;
import org.example.teacherauth.persistence.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.teacherauth.persistence.LoginAuthMapper;
import org.example.teacherauth.persistence.UserMapper;
import org.example.teacherauth.service.UserService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private  UserMapper userMapper;
    @Autowired
    private  LoginAuthMapper loginAuthMapper;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    @Transactional
    public User createUser(UserCreateDTO userCreateDTO) {

        // 1. 创建用户基本信息（不包含密码）
        User user = new User();
        user.setUsername(userCreateDTO.getUsername());
        user.setPhone(userCreateDTO.getPhone());
        user.setUserType(userCreateDTO.getType());
        user.setDepartmentId(userCreateDTO.getDepartmentId());
        user.setRealName(userCreateDTO.getRealName());
        user.setEmail(userCreateDTO.getEmail());
        userMapper.insert(user); // 插入后user会自动获得生成的user_id

        // 2. 创建登录认证信息（密码单独存储）
        LoginAuth loginAuth = new LoginAuth();
        loginAuth.setUserId(user.getUserId());
        // 密码加密存储（使用BCrypt）
        loginAuth.setPassword(userCreateDTO.getPassword());
        loginAuth.setPasswordHash(passwordEncoder.encode(userCreateDTO.getPassword()));
        loginAuth.setLastLogin(new Date()); // 直接使用java.util.Date

        loginAuthMapper.insert(loginAuth);
        user.setDepartmentName(this.getDepartmentNameById(userCreateDTO.getDepartmentId()));
        System.out.println("院系名字："+user.getDepartmentName());
        return user;
    }

    @Override
    public User getUserById(String userId) {
        User user = userMapper.selectById(Integer.valueOf(userId));
        System.out.println("通过用户ID找User用户===========================");
        System.out.println(user.getDepartmentId());
        System.out.println(user.getUserId());
        System.out.println(user.getRealName());
        if(user.getDepartmentId() != null) {
            user.setDepartmentName(this.getDepartmentNameById(user.getDepartmentId()));
        }
        System.out.println("院系ID"+user.getDepartmentId());
        System.out.println("院系名字："+user.getDepartmentName());
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUsername, username));
        user.setDepartmentName(this.getDepartmentNameById(user.getDepartmentId()));
        System.out.println("院系名字："+user.getDepartmentName());
        return user;
    }

    @Override
    public User updateUser(User user) {
        User newUser = getUserById(String.valueOf(user.getUserId()));
        if(newUser == null) {
            return null;
        }
        if(user.getUserId()==null){
            user.setUserId(newUser.getUserId());
        }
        if(user.getRealName() == null) {
            user.setRealName(newUser.getRealName());
        }
        if(user.getEmail() == null) {
            user.setEmail(newUser.getEmail());
        }
        if(user.getDepartmentId() == null) {
            user.setDepartmentId(newUser.getDepartmentId());
        }
        if(user.getPhone() == null) {
            user.setPhone(newUser.getPhone());
        }
        if(user.getUserType() == null) {
            user.setUserType(newUser.getUserType());
        }
        userMapper.updateById(user);
        user.setDepartmentName(this.getDepartmentNameById(user.getDepartmentId()));
        System.out.println("院系名字："+user.getDepartmentName());
        return user;
    }

    @Override
    @Transactional
    public void deleteUser(Integer userId) {
        // 先删除登录认证信息（因为有外键约束）
        loginAuthMapper.delete(Wrappers.<LoginAuth>lambdaQuery()
                .eq(LoginAuth::getUserId, userId));

        // 再删除用户信息
        userMapper.deleteById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        // 1. 查询所有用户
        List<User> users = userMapper.selectList(null);

        // 2. 遍历每个用户，设置院系名称
        for (User user : users) {
            if (user.getDepartmentId() != null) {
                // 为每个用户单独查询院系名称
                String deptName = departmentMapper.selectDepartmentNameById(user.getDepartmentId());
                user.setDepartmentName(deptName);
                System.out.println("院系名字："+user.getDepartmentName());
                System.out.println(user.getUserId());
                System.out.println(user.getUsername());
            }
        }

        return users;
    }

    @Override
    public List<User> getUsersByDepartment(Integer departmentId) {

        // 1. 查询所有用户
        List<User> users = userMapper.selectList(Wrappers.<User>lambdaQuery()
                .eq(User::getDepartmentId, departmentId));

        // 2. 遍历每个用户，设置院系名称
        for (User user : users) {
            if (user.getDepartmentId() != null) {
                user.setDepartmentName(departmentMapper.selectDepartmentNameById(departmentId));
                System.out.println("院系名字："+user.getDepartmentName());
            }
        }
        return users;
    }

    @Override
    public List<User> getUsersByType(String userType) {
        return userMapper.selectList(Wrappers.<User>lambdaQuery()
                .eq(User::getUserType, userType));
    }

    @Override
    @Transactional
    public void resetPassword(Integer userId, String newPassword) {
        // 参数校验
        if (userId == null || newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("参数不能为空");
        }

        // 构建更新对象
        LoginAuth loginAuth = new LoginAuth();
        loginAuth.setPassword(newPassword); // 仍然存储明文
        loginAuth.setPasswordHash(passwordEncoder.encode(newPassword));

        // 验证用户存在
        if (loginAuthMapper.selectCount(
                Wrappers.<LoginAuth>lambdaQuery()
                        .eq(LoginAuth::getUserId, userId)) == 0) {
            throw new IllegalArgumentException("用户不存在");
        }

        // 执行更新
        loginAuthMapper.update(loginAuth,
                Wrappers.<LoginAuth>lambdaUpdate()
                        .eq(LoginAuth::getUserId, userId));
    }

    @Override
    public void updateLastLoginTime(String userId) {
        // 1. 构造更新对象
        LoginAuth loginAuth = new LoginAuth();
        // 将LocalDateTime转换为Date
        Date lastLoginDate = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        loginAuth.setLastLogin(lastLoginDate);

        // 2. 使用MyBatis-Plus的UpdateWrapper进行更新
        loginAuthMapper.update(loginAuth,
                new LambdaUpdateWrapper<LoginAuth>()
                        .eq(LoginAuth::getUserId, Integer.valueOf(userId))
        );

    }

    @Override
    public boolean checkPassword(String userId, String rawPassword) {
        // 1. 根据用户ID查询登录认证信息
        LoginAuth loginAuth = loginAuthMapper.selectOne(
                Wrappers.<LoginAuth>lambdaQuery()
                        .eq(LoginAuth::getUserId, Integer.valueOf(userId))
        );
        System.out.println("------------数据库中信息-------------");
        System.out.println("数据库中明文密码"+loginAuth.getPassword());
        System.out.println("存储哈希"+loginAuth.getPasswordHash());
        System.out.println("输入密码"+rawPassword);
        Boolean isMatch = passwordEncoder.matches(rawPassword, loginAuth.getPasswordHash());
        // 2. 验证密码是否存在且匹配
        return loginAuth != null && isMatch;
    }

    /**
     * 根据院系ID获取院系名称
     * @param departmentId 院系ID
     * @return 院系名称，如果不存在返回null
     */
    public String getDepartmentNameById(Integer departmentId) {
        System.out.println("院系ID"+departmentId);
        String departmentName = departmentMapper.selectDepartmentNameById(departmentId);
        System.out.println("院系名字"+departmentName);
        return departmentName;
    }

    /**
     * 判断用户存不存在
     * @param userId
     * @return
     */
    public Boolean isUserExist(Integer userId) {
        boolean isExist = false;
        User user = userMapper.selectById(userId);
        if(user != null) {
            isExist = true;
        }
        return isExist;
    }



    // 添加通过用户名验证密码的方法
    @Override
    public boolean checkPasswordByUsername(String username, String password) {
        // 实现从数据库通过username验证密码
        LoginAuth auth = loginAuthMapper.findByUsername(username);
        if (auth == null) {
            return false;
        }
        return passwordEncoder.matches(password, auth.getPasswordHash());
    }
}
