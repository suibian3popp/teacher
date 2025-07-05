package org.example.teacherauth;

import org.example.teacherauth.controller.AuthController;
import org.example.teacherauth.dto.*;
import org.example.teacherauth.entity.LoginAuth;
import org.example.teacherauth.entity.User;
import org.example.teacherauth.service.UserService;
import org.example.teachercommon.entity.UserType;
import org.example.teachercommon.utils.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ========== 登录测试 ==========
    @Test
    void login_Success() {
        // 准备测试数据
        LoginRequestDTO request = new LoginRequestDTO();
        request.setUserId(1);
        request.setPassword("password123");

        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setUsername("testuser");
        mockUser.setUserType(UserType.teacher);
        mockUser.setRealName("Test User");

        LoginAuth mockLoginAuth = new LoginAuth();
        mockLoginAuth.setUserId(1);
        mockLoginAuth.setPasswordHash("$2a$10$N9qo8uLOickgx2ZMRZoMy...");

        // 模拟行为
        when(userService.getUserByUsername("testuser")).thenReturn(mockUser);
        when(userService.checkPassword(1, "password123")).thenReturn(true);
        when(jwtTokenProvider.generateToken(anyInt(), anyString(), any()))
                .thenReturn("mock.jwt.token");

        // 执行测试
        ResponseEntity<LoginResponseDTO> response = authController.login(request);

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("mock.jwt.token", response.getBody().getAccessToken());
        assertEquals("Bearer", response.getBody().getTokenType());
        assertEquals(1, response.getBody().getUserId());
        assertEquals("testuser", response.getBody().getUsername());
        assertEquals(UserType.teacher, response.getBody().getUserType());
        assertEquals("Test User", response.getBody().getRealName());

        // 验证方法调用
        verify(userService).getUserByUsername("testuser");
        verify(userService).checkPassword(1, "password123");
        verify(userService).updateLastLoginTime(1);
        verify(jwtTokenProvider).generateToken(1, "testuser", UserType.teacher);
    }

    @Test
    void login_UserNotFound() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setUserId(1);
        request.setPassword("password123");

        when(userService.getUserById(1)).thenReturn(null);

        ResponseEntity<LoginResponseDTO> response = authController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).getUserById(1);
        verify(userService, never()).checkPassword(anyInt(), anyString());
    }

    @Test
    void login_WrongPassword() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setUserId(1);
        request.setPassword("wrongpassword");

        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setUsername("testuser");

        when(userService.getUserByUsername("testuser")).thenReturn(mockUser);
        when(userService.checkPassword(1, "wrongpassword")).thenReturn(false);

        ResponseEntity<LoginResponseDTO> response = authController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).getUserById(1);
        verify(userService).checkPassword(1, "wrongpassword");
        verify(userService, never()).updateLastLoginTime(anyInt());
    }

    // ========== 注册测试 ==========
    @Test
    void register_Success() {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setUsername("newuser");
        userCreateDTO.setPassword("password123");
        userCreateDTO.setRealName("New User");
        userCreateDTO.setType(UserType.teacher);
        userCreateDTO.setEmail("newuser@example.com");

        User mockUser = new User();
        mockUser.setUserId(2);
        mockUser.setUsername("newuser");
        mockUser.setRealName("New User");
        mockUser.setUserType(UserType.teacher);
        mockUser.setEmail("newuser@example.com");

        when(userService.createUser(userCreateDTO)).thenReturn(mockUser);
        // 移除 passwordEncoder.encode 的验证，因为这是在服务层处理的

        ResponseEntity<User> response = authController.register(userCreateDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getUserId());
        assertEquals("newuser", response.getBody().getUsername());
        assertEquals(UserType.teacher, response.getBody().getUserType());
        assertEquals("newuser@example.com", response.getBody().getEmail());

        verify(userService).createUser(userCreateDTO);
        // 不再验证 passwordEncoder.encode
    }

    // ========== 用户管理测试 ==========
    @Test
    void getAllUsers_Success() {
        User user1 = new User();
        user1.setUserId(1);
        user1.setUsername("user1");

        User user2 = new User();
        user2.setUserId(2);
        user2.setUsername("user2");

        List<User> userList = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(userList);

        ResponseEntity<List<User>> response = authController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(1, response.getBody().get(0).getUserId());
        assertEquals(2, response.getBody().get(1).getUserId());

        verify(userService).getAllUsers();
    }

    @Test
    void getUserById_Success() {
        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setUsername("testuser");
        mockUser.setEmail("test@example.com");

        when(userService.getUserById(1)).thenReturn(mockUser);

        ResponseEntity<User> response = authController.getUserById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getUserId());
        assertEquals("testuser", response.getBody().getUsername());
        assertEquals("test@example.com", response.getBody().getEmail());

        verify(userService).getUserById(1);
    }

    @Test
    void updateUser_Success() {
        User userToUpdate = new User();
        userToUpdate.setUserId(1);
        userToUpdate.setUsername("updateduser");
        userToUpdate.setRealName("Updated User");
        userToUpdate.setEmail("updated@example.com");

        when(userService.updateUser(userToUpdate)).thenReturn(userToUpdate);

        ResponseEntity<User> response = authController.updateUser(1, userToUpdate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getUserId());
        assertEquals("updateduser", response.getBody().getUsername());
        assertEquals("updated@example.com", response.getBody().getEmail());

        verify(userService).updateUser(userToUpdate);
    }

    @Test
    void deleteUser_Success() {
        ResponseEntity<Void> response = authController.deleteUser(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(userService).deleteUser(1);
    }

    // ========== 查询测试 ==========
    @Test
    void getUsersByDepartment_Success() {
        User user1 = new User();
        user1.setUserId(1);
        user1.setDepartmentId(101);

        User user2 = new User();
        user2.setUserId(2);
        user2.setDepartmentId(101);

        List<User> userList = Arrays.asList(user1, user2);

        when(userService.getUsersByDepartment(101)).thenReturn(userList);

        ResponseEntity<List<User>> response = authController.getUsersByDepartment(101);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(101, response.getBody().get(0).getDepartmentId());
        assertEquals(101, response.getBody().get(1).getDepartmentId());

        verify(userService).getUsersByDepartment(101);
    }

    @Test
    void getUsersByType_Success() {
        User user1 = new User();
        user1.setUserId(1);
        user1.setUserType(UserType.teacher);

        User user2 = new User();
        user2.setUserId(2);
        user2.setUserType(UserType.teacher);

        List<User> userList = Arrays.asList(user1, user2);

        when(userService.getUsersByType("TEACHER")).thenReturn(userList);

        ResponseEntity<List<User>> response = authController.getUsersByType("TEACHER");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(UserType.teacher, response.getBody().get(0).getUserType());
        assertEquals(UserType.teacher, response.getBody().get(1).getUserType());

        verify(userService).getUsersByType("TEACHER");
    }

    // ========== 密码管理测试 ==========
    @Test
    void resetPassword_Success() {
        when(userService.resetPassword(1, "newPassword123")).thenReturn(true);

        ResponseEntity<Void> response = authController.resetPassword(1, "newPassword123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        verify(userService).resetPassword(1, "newPassword123");
    }
}