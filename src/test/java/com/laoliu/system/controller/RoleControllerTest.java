package com.laoliu.system.controller;

import com.laoliu.system.mapper.UserMapper;
import com.laoliu.system.service.RoleService;
import com.laoliu.system.utils.JWTUtils;
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
class RoleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoleService roleService;

    @Mock
    private JWTUtils jwtUtils;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private RoleController roleController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
    }

    @Test
    @DisplayName("获取用户角色成功")
    void getRole_success_shouldReturnRole() throws Exception {
        String token = "Bearer test-token";
        Claims claims = mock(Claims.class);

        when(jwtUtils.parseToken("test-token")).thenReturn(claims);
        when(claims.getSubject()).thenReturn("1");
        when(roleService.getRoleByUserId(1L)).thenReturn("管理员");

        mockMvc.perform(get("/role")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value("管理员"));
    }
}
