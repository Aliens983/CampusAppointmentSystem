package com.laoliu.system.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRoleEnumTest {

    @Test
    @DisplayName("getByCode 应返回正确的枚举")
    void getByCode_shouldReturnCorrectEnum() {
        assertEquals(UserRoleEnum.USER, UserRoleEnum.getByCode(0));
        assertEquals(UserRoleEnum.ADMIN, UserRoleEnum.getByCode(1));
        assertEquals(UserRoleEnum.SUPER_ADMIN, UserRoleEnum.getByCode(2));
    }

    @Test
    @DisplayName("getByCode 对未知code应返回USER")
    void getByCode_shouldReturnUserForUnknownCode() {
        assertEquals(UserRoleEnum.USER, UserRoleEnum.getByCode(99));
        assertEquals(UserRoleEnum.USER, UserRoleEnum.getByCode(-1));
    }

    @Test
    @DisplayName("code和description应正确")
    void enumFields_shouldBeCorrect() {
        assertEquals(0, UserRoleEnum.USER.getCode());
        assertEquals("普通用户", UserRoleEnum.USER.getDescription());
        assertEquals(1, UserRoleEnum.ADMIN.getCode());
        assertEquals("管理员", UserRoleEnum.ADMIN.getDescription());
        assertEquals(2, UserRoleEnum.SUPER_ADMIN.getCode());
        assertEquals("超级管理员", UserRoleEnum.SUPER_ADMIN.getDescription());
    }

    @Test
    @DisplayName("hasPermission null角色应返回false")
    void hasPermission_nullRole_shouldReturnFalse() {
        assertFalse(UserRoleEnum.hasPermission(null, UserRoleEnum.USER));
    }

    @Test
    @DisplayName("hasPermission USER级别应能访问USER接口")
    void hasPermission_userShouldAccessUserResources() {
        assertTrue(UserRoleEnum.hasPermission("0", UserRoleEnum.USER));
    }

    @Test
    @DisplayName("hasPermission USER不能访问ADMIN接口")
    void hasPermission_userCannotAccessAdminResources() {
        assertFalse(UserRoleEnum.hasPermission("0", UserRoleEnum.ADMIN));
    }

    @Test
    @DisplayName("hasPermission ADMIN可以访问USER和ADMIN接口")
    void hasPermission_adminCanAccessUserAndAdminResources() {
        assertTrue(UserRoleEnum.hasPermission("1", UserRoleEnum.USER));
        assertTrue(UserRoleEnum.hasPermission("1", UserRoleEnum.ADMIN));
    }

    @Test
    @DisplayName("hasPermission ADMIN不能访问SUPER_ADMIN接口")
    void hasPermission_adminCannotAccessSuperAdminResources() {
        assertFalse(UserRoleEnum.hasPermission("1", UserRoleEnum.SUPER_ADMIN));
    }

    @Test
    @DisplayName("hasPermission SUPER_ADMIN可以访问所有接口")
    void hasPermission_superAdminCanAccessAllResources() {
        assertTrue(UserRoleEnum.hasPermission("2", UserRoleEnum.USER));
        assertTrue(UserRoleEnum.hasPermission("2", UserRoleEnum.ADMIN));
        assertTrue(UserRoleEnum.hasPermission("2", UserRoleEnum.SUPER_ADMIN));
    }

    @Test
    @DisplayName("hasPermission 同时传入多个requiredRoles 满足一个即可")
    void hasPermission_multipleRoles_anyMatchShouldPass() {
        assertTrue(UserRoleEnum.hasPermission("0", UserRoleEnum.ADMIN, UserRoleEnum.USER));
        assertFalse(UserRoleEnum.hasPermission("0", UserRoleEnum.ADMIN, UserRoleEnum.SUPER_ADMIN));
    }
}
