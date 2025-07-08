package org.example.teacherauth.service;

import org.example.teacherauth.dto.UserCreateDTO;
import org.example.teacherauth.entity.User;

import java.util.List;

public interface UserService {
    /**
     * 创建新用户
     * @param userCreateDTO 用户创建请求
     * @return 创建成功的用户信息
     */
    User createUser(UserCreateDTO userCreateDTO);

    /**
     * 根据用户ID查询用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    User getUserById(String userId);
    
    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);

    /**
     * 更新用户信息
     * @param user 更新后的用户信息
     * @return 更新成功的用户信息
     */
    User updateUser(User user);

    /**
     * 删除用户
     * @param userId 用户ID
     * @return 是否删除成功
     */
    void deleteUser(Integer userId);

    /**
     * 查询所有用户
     * @return 用户列表
     */
    List<User> getAllUsers();

    /**
     * 根据部门查询用户
     * @param departmentId 部门ID
     * @return 该部门下的用户列表
     */
    List<User> getUsersByDepartment(Integer departmentId);

    /**
     * 根据用户类型查询用户
     * @param userType 用户类型
     * @return 该类型的用户列表
     */
    List<User> getUsersByType(String userType);

    /**
     * 重置用户密码
     * @param userId 用户ID
     * @param newPassword 新密码
     * @return 是否重置成功
     */
    void resetPassword(Integer userId, String newPassword);

    /**
     * 更新登录时间
     * @param userId
     */
    void updateLastLoginTime(String userId);

    /**
     * 校验密码
     * @param userId 用户ID
     * @param rawPassword 原始密码
     * @return 密码是否匹配
     */
    boolean checkPassword(String userId, String rawPassword);

    // 添加新方法：验证用户名密码
    boolean checkPasswordByUsername(String username, String password);
}