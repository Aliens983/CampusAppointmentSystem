package com.laoliu.system.service;

import com.laoliu.system.mapper.UserMapper;
import com.laoliu.system.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private UserMapper userMapper;

    private RoleServiceImpl roleService;

    @BeforeEach
    void setUp() {
        roleService = new RoleServiceImpl(userMapper);
    }

    @Test
    @DisplayName("getRoleByUserId 普通用户应返回'普通用户'")
    void getRoleByUserId_user_shouldReturnUser() {
        when(userMapper.getRoleByUserId(1L)).thenReturn("0");
        assertEquals("普通用户", roleService.getRoleByUserId(1L));
    }

    @Test
    @DisplayName("getRoleByUserId 管理员应返回'管理员'")
    void getRoleByUserId_admin_shouldReturnAdmin() {
        when(userMapper.getRoleByUserId(1L)).thenReturn("1");
        assertEquals("管理员", roleService.getRoleByUserId(1L));
    }

    @Test
    @DisplayName("getRoleByUserId 未知角色应返回原始值")
    void getRoleByUserId_unknownRole_shouldReturnRaw() {
        when(userMapper.getRoleByUserId(1L)).thenReturn("2");
        assertEquals("2", roleService.getRoleByUserId(1L));
    }

    @Test
    @DisplayName("getRoleByUserId null角色应返回null")
    void getRoleByUserId_null_shouldReturnNull() {
        when(userMapper.getRoleByUserId(1L)).thenReturn(null);
        assertNull(roleService.getRoleByUserId(1L));
    }

    @Test
    @DisplayName("changeRoleById 普通用户升级为管理员")
    void changeRoleById_userToAdmin() {
        when(userMapper.getRoleByUserId(1L)).thenReturn("0");

        String result = roleService.changeRoleById(1L);

        assertEquals("管理员用户", result);
        verify(userMapper).updateRoleToAdmin(1L);
        verify(userMapper, never()).updateRoleToCommonUser(anyLong());
    }

    @Test
    @DisplayName("changeRoleById 管理员降级为普通用户")
    void changeRoleById_adminToUser() {
        when(userMapper.getRoleByUserId(1L)).thenReturn("1");

        String result = roleService.changeRoleById(1L);

        assertEquals("普通用户", result);
        verify(userMapper).updateRoleToCommonUser(1L);
        verify(userMapper, never()).updateRoleToAdmin(anyLong());
    }

    @Test
    @DisplayName("changeRoleById 未知角色应返回原始值且不修改")
    void changeRoleById_unknownRole_shouldReturnRaw() {
        when(userMapper.getRoleByUserId(1L)).thenReturn("2");

        String result = roleService.changeRoleById(1L);

        assertEquals("2", result);
        verify(userMapper, never()).updateRoleToAdmin(anyLong());
        verify(userMapper, never()).updateRoleToCommonUser(anyLong());
    }
}
