package org.example.teacherauth.controller;

import org.example.teacherauth.dto.LoginRequestDTO;
import org.example.teacherauth.dto.LoginResponseDTO;
import org.example.teacherauth.dto.UserCreateDTO;
import org.example.teacherauth.entity.User;

import org.example.teachercommon.utils.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.example.teacherauth.service.UserService;

import java.util.List;

/**
 * 用户认证和管理的REST控制器
 * 处理所有与用户相关的操作，包括登录、注册、用户信息管理等
 */
@RestController
@RequestMapping("auth")
public class AuthController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService userService,
                          JwtTokenProvider jwtTokenProvider,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 用户登录接口
     * @param request 登录请求体，包含用户名和密码
     * @return 登录成功返回JWT令牌和用户信息，失败返回401未授权
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        System.out.println("进入登录功能");
        // 1. 根据用户名查找用户
        User user = userService.getUserById(request.getUserId());

        // 用户不存在返回401
        if (user == null) {
            System.out.println("用户不存在");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // 2. 验证密码是否正确
        if (!userService.checkPassword(user.getUserId(), request.getPassword())) {
            System.out.println("密码不正确");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 3. 更新用户最后登录时间
        userService.updateLastLoginTime(user.getUserId());

        // 4. 生成JWT令牌
        String token = jwtTokenProvider.generateToken(
                user.getUserId(),
                user.getUsername(),
                user.getUserType()
        );

        // 5. 返回令牌和用户信息
        return ResponseEntity.ok(
                new LoginResponseDTO(
                        token,
                        user.getUserId(),
                        user.getUsername(),
                        user.getUserType(),
                        user.getRealName(),
                        user.getDepartmentName()
                )
        );
    }

    /**
     * 用户注册接口
     * @param userCreateDTO 用户注册信息
     * @return 注册成功返回创建的用户信息，状态码201
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserCreateDTO userCreateDTO) {
        User createdUser = userService.createUser(userCreateDTO);
        System.out.print(createdUser.getUsername());
        System.out.print(createdUser.getUserId());
        System.out.print(createdUser.getRealName());
        System.out.print(createdUser.getUserType());
        System.out.print(createdUser.getDepartmentName());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * 获取所有用户列表
     * @return 用户列表，状态码200
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        System.out.println("进入获取所有用户列表功能");
        List<User> users = userService.getAllUsers();
        System.out.println("---------------结果------------");
        for (User user : users) {
            System.out.println(user.getUsername());
            System.out.println(user.getUserId());
        }
        return ResponseEntity.ok(users);
    }

    /**
     * 根据用户ID获取用户详情
     * @param userId 用户ID
     * @return 用户详细信息（存在返回200，不存在返回404）
     */
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build(); // 返回404
        }
        return ResponseEntity.ok(user); // 返回200 + 用户数据
    }

    /**
     * 更新用户信息
     * @param userId 要更新的用户ID
     * @param user 更新后的用户信息
     * @return 更新后的用户信息，状态码200
     */
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(
            @PathVariable Integer userId,
            @RequestBody User user) {
        user.setUserId(userId); // 确保ID一致
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * 删除用户
     * @param userId 要删除的用户ID
     * @return 无内容，状态码204
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 根据部门ID查询用户列表
     * @param departmentId 部门ID
     * @return 该部门下的用户列表，状态码200
     */
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<User>> getUsersByDepartment(
            @PathVariable Integer departmentId) {
        System.out.println("进入根据部门ID查询用户列表功能");
        List<User> users = userService.getUsersByDepartment(departmentId);
        return ResponseEntity.ok(users);
    }

    /**
     * 根据用户类型查询用户列表
     * @param userType 用户类型(teacher/ta/department_admin)
     * @return 该类型的用户列表，状态码200
     */
    @GetMapping("/type/{userType}")
    public ResponseEntity<List<User>> getUsersByType(
            @PathVariable String userType) {
        System.out.println("进入根据用户类型查询用户列表功能");
        List<User> users = userService.getUsersByType(userType);
        return ResponseEntity.ok(users);
    }

    /**
     * 重置用户密码
     * @param userId 用户ID
     * @param newPassword 新密码
     * @return 无内容，状态码200
     */
    @PostMapping("/{userId}/reset-password")
    public ResponseEntity<Void> resetPassword(
            @PathVariable Integer userId,
            @RequestParam String newPassword) {
        System.out.println("进入重置用户密码功能");
        userService.resetPassword(userId, newPassword);
        return ResponseEntity.ok().build();
    }
}