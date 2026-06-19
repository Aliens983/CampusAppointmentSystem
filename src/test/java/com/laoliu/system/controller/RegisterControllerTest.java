package com.laoliu.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laoliu.system.converter.UserConverter;
import com.laoliu.system.entity.User;
import com.laoliu.system.mapper.UserMapper;
import com.laoliu.system.utils.JWTUtils;
import com.laoliu.system.utils.PasswordUtils;
import com.laoliu.system.utils.RedisUtil;
import com.laoliu.system.vo.request.UserRegisterRequest;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RegisterControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private RedisUtil redisUtil;

    @Mock
    private JWTUtils jwtUtils;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserConverter userConverter;

    @Mock
    private PasswordUtils passwordUtils;

    @InjectMocks
    private RegisterController registerController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(registerController).build();
    }

    @Test
    @DisplayName("验证邮箱验证码并注册成功")
    void verifyEmailCode_success_shouldReturnToken() throws Exception {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setEmail("test@test.com");
        request.setCode("123456");
        request.setPassword("password123");
        request.setName("张三");
        request.setGrade("2024级");
        request.setSex("男");
        request.setAge(20);
        request.setRole(0);

        when(userMapper.getUserIdByEmail("test@test.com")).thenReturn(null);
        when(redisUtil.getVerificationCode("test@test.com")).thenReturn("123456");
        when(passwordUtils.encode("password123")).thenReturn("encodedPassword");

        User mockUser = new User();
        mockUser.setId(1L);
        when(userConverter.convertUserRequestToUser(any())).thenReturn(mockUser);
        when(jwtUtils.generateToken(1L)).thenReturn("test-token");

        mockMvc.perform(post("/register/verify-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value("test-token"));

        verify(userMapper).insertSelective(any());
    }

    @Test
    @DisplayName("验证邮箱验证码时邮箱或验证码为空应返回400")
    void verifyEmailCode_missingParams_shouldReturn400() throws Exception {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setEmail(null);
        request.setCode(null);

        mockMvc.perform(post("/register/verify-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    @DisplayName("验证邮箱验证码时用户已存在应返回409")
    void verifyEmailCode_userExists_shouldReturn409() throws Exception {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setEmail("existing@test.com");
        request.setCode("123456");
        request.setPassword("password123");

        when(userMapper.getUserIdByEmail("existing@test.com")).thenReturn(1L);

        mockMvc.perform(post("/register/verify-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(409));
    }

    @Test
    @DisplayName("验证邮箱验证码时验证码不存在应返回401")
    void verifyEmailCode_codeExpired_shouldReturn401() throws Exception {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setEmail("test@test.com");
        request.setCode("123456");
        request.setPassword("password123");

        when(userMapper.getUserIdByEmail("test@test.com")).thenReturn(null);
        when(redisUtil.getVerificationCode("test@test.com")).thenReturn(null);

        mockMvc.perform(post("/register/verify-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(401));
    }

    @Test
    @DisplayName("验证邮箱验证码时验证码错误应返回401")
    void verifyEmailCode_wrongCode_shouldReturn401() throws Exception {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setEmail("test@test.com");
        request.setCode("wrong");
        request.setPassword("password123");

        when(userMapper.getUserIdByEmail("test@test.com")).thenReturn(null);
        when(redisUtil.getVerificationCode("test@test.com")).thenReturn("123456");

        mockMvc.perform(post("/register/verify-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(401));
    }
}
