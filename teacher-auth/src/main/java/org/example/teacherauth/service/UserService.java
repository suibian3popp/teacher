package org.example.teacherauth.service;



import org.example.teacherauth.dto.UserCreateDTO;
import org.example.teacherauth.entity.User;


import java.util.List;


public interface UserService {
    /**
     * 创建新用户
     * @param userCreateDTO 用户创建DTO
     * @return 创建成功的用户信息
     */
    User createUser(UserCreateDTO userCreateDTO);

    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    User getUserById(Integer userId);

    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 更新后的用户信息
     */
    User updateUser(User user);

    /**
     * 删除用户
     * @param userId 用户ID
     * @return 是否删除成功
     */
    boolean deleteUser(Integer userId);

    /**
     * 获取所有用户列表
     * @return 用户列表
     */
    List<User> getAllUsers();

    /**
     * 根据院系ID获取用户列表
     * @param departmentId 院系ID
     * @return 用户列表
     */
    List<User> getUsersByDepartment(Integer departmentId);

    /**
     * 根据用户类型获取用户列表
     * @param userType 用户类型
     * @return 用户列表
     */
    List<User> getUsersByType(String userType);

    /**
     * 重置用户密码
     * @param userId 用户ID
     * @param newPassword 新密码
     * @return 是否重置成功
     */
    boolean resetPassword(Integer userId, String newPassword);

    /**
     * 更新登录时间
     * @param userId
     */
    void updateLastLoginTime(Integer userId);

    /**
     * 验证用户密码是否正确
     * @param userId 用户ID
     * @param rawPassword 待验证的原始密码
     * @return 密码是否匹配
     */
    boolean checkPassword(Integer userId, String rawPassword);
}