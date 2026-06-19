package com.laoliu.system.service;

import com.laoliu.system.entity.User;
import com.laoliu.system.exception.ResourceNotFoundException;
import com.laoliu.system.mapper.UserMapper;
import com.laoliu.system.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl();
        ReflectionTestUtils.setField(userService, "baseMapper", userMapper);
    }

    @Test
    @DisplayName("getUserInfoAndBookings 用户不存在应抛出异常")
    void getUserInfoAndBookings_userNotFound_shouldThrow() {
        when(userMapper.selectById(1L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserInfoAndBookings(1L));
    }
}
