package com.laoliu.system.controller;

import com.laoliu.system.api.GetUserIdViaTokenApi;
import com.laoliu.system.converter.UserConverter;
import com.laoliu.system.entity.User;
import com.laoliu.system.mapper.UserMapper;
import com.laoliu.system.service.UserService;
import com.laoliu.system.utils.JWTUtils;
import com.laoliu.system.utils.PasswordUtils;
import com.laoliu.system.vo.response.UserResponse;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private JWTUtils jwtUtils;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserConverter userConverter;

    @Mock
    private PasswordUtils passwordUtils;

    @Mock
    private GetUserIdViaTokenApi getUserIdViaTokenApi;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("获取用户信息成功")
    void getUserByParseToken_success_shouldReturnUser() throws Exception {
        String token = "Bearer test-token";
        Claims claims = mock(Claims.class);

        when(jwtUtils.parseToken("test-token")).thenReturn(claims);
        when(claims.getSubject()).thenReturn("1");

        User user = new User();
        user.setId(1L);
        user.setName("张三");
        user.setEmail("test@test.com");
        user.setRole(0);

        when(userMapper.selectByPrimaryKey(1L)).thenReturn(user);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setName("张三");
        userResponse.setEmail("test@test.com");
        userResponse.setRole("User");

        when(userConverter.convertUserToUserResponse(user)).thenReturn(userResponse);

        mockMvc.perform(get("/user")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.email").value("test@test.com"));
    }
}
