package com.laoliu.cas.system.application.service.impl;

import com.laoliu.cas.system.application.service.RoleService;
import com.laoliu.cas.system.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * RoleServiceImpl 单元测试。
 *
 * @author forever-king
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("角色服务单元测试")
class RoleServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private RoleService roleService;

    private static final Long USER_ID = 1L;

    @BeforeEach
    void setUp() {
        roleService = new RoleServiceImpl(userRepository);
    }

    // ======================== getRoleByUserId ========================

    @Nested
    @DisplayName("获取用户角色 - getRoleByUserId")
    class GetRoleByUserIdTests {

        @Test
        @DisplayName("role=0 时应当返回\"普通用户\"")
        void shouldReturnCommonUserWhenRoleIs0() {
            // Given
            when(userRepository.getRoleByUserId(USER_ID)).thenReturn("0");

            // When
            String result = roleService.getRoleByUserId(USER_ID);

            // Then
            assertEquals("普通用户", result);
        }

        @Test
        @DisplayName("role=1 时应当返回\"管理员\"")
        void shouldReturnAdminWhenRoleIs1() {
            // Given
            when(userRepository.getRoleByUserId(USER_ID)).thenReturn("1");

            // When
            String result = roleService.getRoleByUserId(USER_ID);

            // Then
            assertEquals("管理员", result);
        }

        @Test
        @DisplayName("role 为 null 时应当返回 null")
        void shouldReturnNullWhenRoleIsNull() {
            // Given
            when(userRepository.getRoleByUserId(USER_ID)).thenReturn(null);

            // When
            String result = roleService.getRoleByUserId(USER_ID);

            // Then
            assertNull(result);
        }
    }

    // ======================== changeRoleById ========================

    @Nested
    @DisplayName("切换用户角色 - changeRoleById")
    class ChangeRoleByIdTests {

        @Test
        @DisplayName("当前 role=0 时应当切换为管理员并返回\"管理员用户\"")
        void shouldChangeToAdminWhenRoleIs0() {
            // Given
            when(userRepository.getRoleByUserId(USER_ID)).thenReturn("0");

            // When
            String result = roleService.changeRoleById(USER_ID);

            // Then
            assertEquals("管理员用户", result);
            verify(userRepository).updateRoleToAdmin(USER_ID);
            verify(userRepository, never()).updateRoleToCommonUser(anyLong());
        }

        @Test
        @DisplayName("当前 role=1 时应当切换为普通用户并返回\"普通用户\"")
        void shouldChangeToCommonUserWhenRoleIs1() {
            // Given
            when(userRepository.getRoleByUserId(USER_ID)).thenReturn("1");

            // When
            String result = roleService.changeRoleById(USER_ID);

            // Then
            assertEquals("普通用户", result);
            verify(userRepository).updateRoleToCommonUser(USER_ID);
            verify(userRepository, never()).updateRoleToAdmin(anyLong());
        }

        @Test
        @DisplayName("role 为 null 时不作任何处理并返回 null")
        void shouldReturnNullWhenRoleIsNull() {
            // Given
            when(userRepository.getRoleByUserId(USER_ID)).thenReturn(null);

            // When
            String result = roleService.changeRoleById(USER_ID);

            // Then
            assertNull(result);
            verify(userRepository, never()).updateRoleToAdmin(anyLong());
            verify(userRepository, never()).updateRoleToCommonUser(anyLong());
        }
    }
}
