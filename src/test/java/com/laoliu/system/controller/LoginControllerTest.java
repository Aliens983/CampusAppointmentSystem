package com.laoliu.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laoliu.system.mapper.UserMapper;
import com.laoliu.system.utils.JWTUtils;
import com.laoliu.system.utils.PasswordUtils;
import com.laoliu.system.utils.RedisUtil;
import com.laoliu.system.vo.request.ResetPasswordRequest;
import com.laoliu.system.vo.request.UserLoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserMapper userMapper;

    @Mock
    private JWTUtils jwtUtils;

    @Mock
    private PasswordUtils passwordUtils;

    @Mock
    private RedisUtil redisUtil;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
    }

    @Test
    @DisplayName("登录成功应返回token")
    void login_success_shouldReturnToken() throws Exception {
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail("test@test.com");
        request.setPassword("password");

        when(userMapper.getEncodePasswordByEmail("test@test.com")).thenReturn("encodedPassword");
        when(passwordUtils.matches("password", "encodedPassword")).thenReturn(true);
        when(userMapper.getUserIdByEmail("test@test.com")).thenReturn(1L);
        when(jwtUtils.generateToken(1L)).thenReturn("test-token");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value("test-token"));
    }

    @Test
    @DisplayName("登录时邮箱或密码为空应返回400")
    void login_missingEmailOrPassword_shouldReturn400() throws Exception {
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail(null);
        request.setPassword(null);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    @DisplayName("登录时用户不存在应返回404")
    void login_userNotFound_shouldReturn404() throws Exception {
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail("nonexist@test.com");
        request.setPassword("password");

        when(userMapper.getEncodePasswordByEmail("nonexist@test.com")).thenReturn(null);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    @DisplayName("登录时密码错误应返回401")
    void login_wrongPassword_shouldReturn401() throws Exception {
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail("test@test.com");
        request.setPassword("wrong");

        when(userMapper.getEncodePasswordByEmail("test@test.com")).thenReturn("encodedPassword");
        when(passwordUtils.matches("wrong", "encodedPassword")).thenReturn(false);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(401));
    }

    @Test
    @DisplayName("重置密码成功应返回token")
    void resetPassword_success_shouldReturnToken() throws Exception {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setEmail("test@test.com");
        request.setCode("123456");
        request.setPassword("newPassword");

        when(redisUtil.getVerificationCode("test@test.com")).thenReturn("123456");
        when(userMapper.getUserIdByEmail("test@test.com")).thenReturn(1L);
        when(passwordUtils.encode("newPassword")).thenReturn("encodedNewPassword");
        when(jwtUtils.generateToken(1L)).thenReturn("new-token");

        mockMvc.perform(post("/login/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value("new-token"));

        verify(userMapper).updatePasswordByEmail("test@test.com", "encodedNewPassword");
        verify(redisUtil).removeVerificationCode("test@test.com");
    }

    @Test
    @DisplayName("重置密码验证码错误应返回400")
    void resetPassword_wrongCode_shouldReturn400() throws Exception {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setEmail("test@test.com");
        request.setCode("wrong");
        request.setPassword("newPassword");

        when(redisUtil.getVerificationCode("test@test.com")).thenReturn("123456");

        mockMvc.perform(post("/login/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    @DisplayName("重置密码验证码过期应返回400")
    void resetPassword_expiredCode_shouldReturn400() throws Exception {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setEmail("test@test.com");
        request.setCode("123456");
        request.setPassword("newPassword");

        when(redisUtil.getVerificationCode("test@test.com")).thenReturn(null);

        mockMvc.perform(post("/login/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400));
    }
}
