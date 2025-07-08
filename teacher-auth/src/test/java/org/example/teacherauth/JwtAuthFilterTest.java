package org.example.teacherauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.example.teacherauth.configs.JwtFilterConfig;
import org.example.teacherauth.utils.JwtAuthFilter;
import org.example.teachercommon.entity.JwtUserInfo;
import org.example.teachercommon.entity.UserType;
import org.example.teachercommon.utils.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import static org.assertj.core.api.Assertions.assertThat;  // AssertJ 方式
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(JwtFilterConfig.class) // 明确导入过滤器配置
public class JwtAuthFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private JwtAuthFilter jwtAuthFilter; // 直接注入过滤器

    @Autowired
    private ObjectMapper objectMapper;

    private String validToken;
    private JwtUserInfo userInfo;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();

        userInfo = JwtUserInfo.builder()
                .userId(1)
                .username("testuser")
                .userType(UserType.ta.name())
                .issuedAt(new Date())
                .expiresAt(new Date(System.currentTimeMillis() + 3600000))
                .build();

        validToken = "mocked.jwt.token";

        // 配置mock行为
        when(jwtTokenProvider.validateToken(eq(validToken))).thenReturn(true);
        when(jwtTokenProvider.parseTokenToUserInfo(eq(validToken))).thenReturn(userInfo);
        when(jwtTokenProvider.validateToken(anyString())).thenAnswer(invocation -> {
            String token = invocation.getArgument(0);
            return token.equals(validToken);
        });
    }

    @Test
    void shouldSetUserInfoForValidToken() throws Exception {
        // 直接测试过滤器
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + validToken);
        request.setRequestURI("/api/test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        jwtAuthFilter.doFilter(request, response, filterChain);

        assertEquals(200, response.getStatus());
        assertEquals(userInfo.getUserId(), request.getAttribute("userId"));
        assertEquals(userInfo.getUsername(), request.getAttribute("username"));
    }

    @Test
    void shouldReturnUnauthorizedForInvalidToken() throws Exception {
        String invalidToken = "invalid_token";

        // 明确模拟无效令牌的行为
        when(jwtTokenProvider.validateToken(invalidToken)).thenReturn(false);
        when(jwtTokenProvider.parseTokenToUserInfo(invalidToken))
                .thenThrow(new JwtException("Invalid token")); // 模拟抛出异常

        mockMvc.perform(get("/api/test")
                        .header("Authorization", "Bearer " + invalidToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("UNAUTHORIZED"))
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    assertThat(content).contains("无效令牌");
                });
    }

    @Test
    void shouldReturnUnauthorizedForMissingHeader() throws Exception {
        mockMvc.perform(get("/api/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.message").value("Authorization头缺失或格式错误"));
    }

    @Test
    void shouldHandleExpiredToken() throws Exception {
        when(jwtTokenProvider.parseTokenToUserInfo(validToken))
                .thenThrow(new ExpiredJwtException(null, null, "Token expired"));

        mockMvc.perform(get("/api/test")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.message").value("令牌已过期，请重新登录"));
    }
}