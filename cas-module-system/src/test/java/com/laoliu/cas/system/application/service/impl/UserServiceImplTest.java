package com.laoliu.cas.system.application.service.impl;

import com.laoliu.cas.system.application.service.UserService;
import com.laoliu.cas.system.domain.entity.User;
import com.laoliu.cas.system.domain.repository.UserRepository;
import com.laoliu.cas.system.interfaces.dto.response.UserInfoAndServicesViaMPRespVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * UserServiceImpl 单元测试。
 *
 * @author forever-king
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("用户服务单元测试")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    private static final Long USER_ID = 1L;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Nested
    @DisplayName("获取用户信息和预约 - getUserInfoAndBookings")
    class GetUserInfoAndBookingsTests {

        @Test
        @DisplayName("应当成功获取用户信息和预约列表")
        void shouldReturnUserInfoAndBookings() {
            // Given
            User user = buildUser(USER_ID);
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

            Map<String, Object> booking1 = new HashMap<>();
            booking1.put("orderId", 100L);
            booking1.put("serviceName", "自习室预约");
            Map<String, Object> booking2 = new HashMap<>();
            booking2.put("orderId", 101L);
            booking2.put("serviceName", "心理咨询");
            List<Map<String, Object>> bookings = List.of(booking1, booking2);
            when(userRepository.getAllBookings(USER_ID)).thenReturn(bookings);

            // When
            UserInfoAndServicesViaMPRespVO result = userService.getUserInfoAndBookings(USER_ID);

            // Then
            assertNotNull(result);
            assertNotNull(result.getUser());
            assertEquals(USER_ID, result.getUser().getId());
            assertEquals("测试用户", result.getUser().getName());
            assertNotNull(result.getBookings());
            assertEquals(2, result.getBookings().size());
            assertEquals("自习室预约", result.getBookings().get(0).get("serviceName"));
            assertEquals("心理咨询", result.getBookings().get(1).get("serviceName"));
        }

        @Test
        @DisplayName("用户不存在时应当返回空的用户信息和空预约列表")
        void shouldReturnNullUserWhenUserNotFound() {
            // Given
            when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
            when(userRepository.getAllBookings(USER_ID)).thenReturn(Collections.emptyList());

            // When
            UserInfoAndServicesViaMPRespVO result = userService.getUserInfoAndBookings(USER_ID);

            // Then
            assertNotNull(result);
            assertNull(result.getUser());
            assertNotNull(result.getBookings());
            assertTrue(result.getBookings().isEmpty());
        }
    }

    // ======================== 辅助方法 ========================

    private User buildUser(Long id) {
        return User.builder()
                .id(id)
                .name("测试用户")
                .email("test@example.com")
                .grade("大三")
                .sex("男")
                .age(21)
                .role(0)
                .build();
    }
}
