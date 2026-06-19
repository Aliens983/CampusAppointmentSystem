package com.laoliu.system.converter;

import com.laoliu.system.entity.User;
import com.laoliu.system.vo.request.UserRegisterRequest;
import com.laoliu.system.vo.response.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserConverterTest {

    private final UserConverter converter = new UserConverter();

    @Test
    @DisplayName("convertUserRequestToUser 应正确转换所有字段")
    void convertUserRequestToUser_shouldMapAllFields() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setName("张三");
        request.setGrade("2024级");
        request.setSex("男");
        request.setAge(20);
        request.setRole(0);
        request.setEmail("zhangsan@test.com");
        request.setPassword("encodedPassword");

        User user = converter.convertUserRequestToUser(request);

        assertEquals("张三", user.getName());
        assertEquals("2024级", user.getGrade());
        assertEquals("男", user.getSex());
        assertEquals(20, user.getAge());
        assertEquals(0, user.getRole());
        assertEquals("zhangsan@test.com", user.getEmail());
        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    @DisplayName("convertUserToUserResponse 应正确转换管理员角色")
    void convertUserToUserResponse_adminRole_shouldSetAdmin() {
        User user = new User();
        user.setEmail("admin@test.com");
        user.setRole(1);

        UserResponse response = converter.convertUserToUserResponse(user);
        assertEquals("admin@test.com", response.getEmail());
        assertEquals("Admin", response.getRole());
    }

    @Test
    @DisplayName("convertUserToUserResponse 应正确转换普通用户角色")
    void convertUserToUserResponse_userRole_shouldSetUser() {
        User user = new User();
        user.setEmail("user@test.com");
        user.setRole(0);

        UserResponse response = converter.convertUserToUserResponse(user);
        assertEquals("user@test.com", response.getEmail());
        assertEquals("User", response.getRole());
    }
}
