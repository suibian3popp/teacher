package org.example.teacherauth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.example.teacherauth.dto.UserCreateDTO;
import org.example.teacherauth.entity.LoginAuth;
import org.example.teacherauth.entity.User;
import org.example.teacherauth.persistence.LoginAuthMapper;
import org.example.teacherauth.persistence.UserMapper;
import org.example.teacherauth.service.impl.UserServiceImpl;
import org.example.teachercommon.entity.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private LoginAuthMapper loginAuthMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private LoginAuth testLoginAuth;
    private UserCreateDTO testUserCreateDTO;

    @BeforeEach
    void setUp() {
        // 初始化测试用户
        testUser = new User();
        testUser.setUserId(1);
        testUser.setUsername("testuser");
        testUser.setUserType(UserType.TA);
        testUser.setRealName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPhone("1234567890");
        testUser.setDepartmentId(100);

        // 初始化登录认证信息
        testLoginAuth = new LoginAuth();
        testLoginAuth.setAuthId(1L);
        testLoginAuth.setUserId(1);
        testLoginAuth.setPassword("plainPassword"); // 新增明文密码
        testLoginAuth.setPasswordHash("encodedPassword");
        testLoginAuth.setLastLogin(new Date());

        // 初始化用户创建DTO
        testUserCreateDTO = new UserCreateDTO();
        testUserCreateDTO.setUsername("newuser");
        testUserCreateDTO.setPassword("password123");
        testUserCreateDTO.setPhone("13800138000");
        testUserCreateDTO.setType(UserType.TA);
        testUserCreateDTO.setDepartmentId(100);
        testUserCreateDTO.setRealName("李四");
        testUserCreateDTO.setEmail("lisi@example.com");
    }

    @Test
    void createUser_ShouldCreateBothUserAndLoginAuthWithPlainAndHashedPassword() {
        // 模拟Mapper行为
        when(userMapper.insert(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setUserId(1); // 模拟ID生成
            return 1;
        });
        when(loginAuthMapper.insert(any(LoginAuth.class))).thenReturn(1);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        // 执行测试
        User createdUser = userService.createUser(testUserCreateDTO);

        // 验证用户信息
        assertNotNull(createdUser);
        assertEquals(1, createdUser.getUserId());
        assertEquals("newuser", createdUser.getUsername());
        assertEquals(UserType.TA, createdUser.getUserType());

        // 验证密码加密
        verify(passwordEncoder).encode("password123");

        // 验证LoginAuth插入
        ArgumentCaptor<LoginAuth> loginAuthCaptor = ArgumentCaptor.forClass(LoginAuth.class);
        verify(loginAuthMapper).insert(loginAuthCaptor.capture());
        LoginAuth savedLoginAuth = loginAuthCaptor.getValue();
        assertEquals(1, savedLoginAuth.getUserId());
        assertEquals("password123", savedLoginAuth.getPassword()); // 验证明文密码
        assertEquals("encodedPassword", savedLoginAuth.getPasswordHash()); // 验证加密密码
        assertNotNull(savedLoginAuth.getLastLogin());
    }

    @Test
    void getUserById_ShouldReturnUser_WhenExists() {
        when(userMapper.selectById(1)).thenReturn(testUser);

        User result = userService.getUserById(1);

        assertNotNull(result);
        assertEquals(1, result.getUserId());
        verify(userMapper).selectById(1);
    }

    @Test
    void getUserByUsername_ShouldReturnUser_WhenExists() {
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testUser);

        User result = userService.getUserByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void updateUser_ShouldUpdateUserInfo() {
        when(userMapper.updateById(testUser)).thenReturn(1);

        User result = userService.updateUser(testUser);

        assertSame(testUser, result);
        verify(userMapper).updateById(testUser);
    }

    @Test
    void deleteUser_ShouldDeleteBothUserAndLoginAuth() {
        when(loginAuthMapper.delete(any(LambdaQueryWrapper.class))).thenReturn(1);
        when(userMapper.deleteById(1)).thenReturn(1);

        boolean result = userService.deleteUser(1);

        assertTrue(result);
        verify(loginAuthMapper).delete(any(LambdaQueryWrapper.class));
        verify(userMapper).deleteById(1);
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        when(userMapper.selectList(null)).thenReturn(Collections.singletonList(testUser));

        List<User> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("testuser", users.get(0).getUsername());
    }

    @Test
    void getUsersByDepartment_ShouldFilterByDepartment() {
        when(userMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(Collections.singletonList(testUser));
        List<User> users = userService.getUsersByDepartment(100);

        assertEquals(1, users.size());
        assertEquals(100, users.get(0).getDepartmentId());
    }

    @Test
    void getUsersByType_ShouldFilterByUserType() {
        when(userMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(Collections.singletonList(testUser));

        List<User> users = userService.getUsersByType("ta");

        assertEquals(1, users.size());
        assertEquals(UserType.TA, users.get(0).getUserType());
    }

    @Test
    void resetPassword_ShouldUpdateBothPlainAndHashedPassword() {
        // 1. 准备测试数据
        String newPassword = "newPassword";
        String encodedPassword = "newEncodedPassword";

        // 2. 配置mock行为
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);
        when(loginAuthMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(1L);

        // 使用ArgumentCaptor捕获参数
        ArgumentCaptor<LoginAuth> loginAuthCaptor = ArgumentCaptor.forClass(LoginAuth.class);
        ArgumentCaptor<LambdaUpdateWrapper<LoginAuth>> wrapperCaptor =
                ArgumentCaptor.forClass(LambdaUpdateWrapper.class);

        when(loginAuthMapper.update(loginAuthCaptor.capture(), wrapperCaptor.capture()))
                .thenReturn(1);

        // 3. 执行测试
        boolean result = userService.resetPassword(1, newPassword);

        // 4. 验证结果
        assertTrue(result);

        // 5. 验证密码编码
        verify(passwordEncoder).encode(newPassword);

        // 6. 验证LoginAuth参数
        LoginAuth capturedLoginAuth = loginAuthCaptor.getValue();
        assertEquals(newPassword, capturedLoginAuth.getPassword()); // 验证明文密码
        assertEquals(encodedPassword, capturedLoginAuth.getPasswordHash()); // 验证加密密码

        // 7. 验证Wrapper条件（可选）
        LambdaUpdateWrapper<LoginAuth> wrapper = wrapperCaptor.getValue();
        assertNotNull(wrapper);
    }

    @Test
    void resetPassword_ShouldReturnFalse_WhenUserNotFound() {
        when(loginAuthMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);

        boolean result = userService.resetPassword(1, "newPassword");

        assertFalse(result);
        verify(loginAuthMapper, never()).update(any(), any());
    }

    @Test
    void updateLastLoginTime_ShouldSetCurrentTime() {
        when(loginAuthMapper.update(any(LoginAuth.class), any(LambdaUpdateWrapper.class))).thenReturn(1);

        userService.updateLastLoginTime(1);

        ArgumentCaptor<LoginAuth> captor = ArgumentCaptor.forClass(LoginAuth.class);
        verify(loginAuthMapper).update(captor.capture(), any(LambdaUpdateWrapper.class));
        assertNotNull(captor.getValue().getLastLogin());
        assertTrue(Math.abs(new Date().getTime() - captor.getValue().getLastLogin().getTime()) < 1000);
    }

    @Test
    void checkPassword_ShouldReturnTrue_WhenPasswordMatches() {
        when(loginAuthMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testLoginAuth);
        when(passwordEncoder.matches("correctPassword", "encodedPassword")).thenReturn(true);

        boolean result = userService.checkPassword(1, "correctPassword");

        assertTrue(result);
    }

    @Test
    void checkPassword_ShouldReturnFalse_WhenPasswordNotMatches() {
        when(loginAuthMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testLoginAuth);
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        boolean result = userService.checkPassword(1, "wrongPassword");

        assertFalse(result);
    }

    @Test
    void checkPassword_ShouldReturnFalse_WhenLoginAuthNotFound() {
        when(loginAuthMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);

        boolean result = userService.checkPassword(1, "anyPassword");

        assertFalse(result);
    }
}