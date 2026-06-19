package com.laoliu.system.service;

import com.laoliu.system.entity.Services;
import com.laoliu.system.entity.User;
import com.laoliu.system.mapper.ItemMapper;
import com.laoliu.system.mapper.ServiceMapper;
import com.laoliu.system.mapper.UserMapper;
import com.laoliu.system.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ServiceMapper serviceMapper;

    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl(itemMapper, userMapper, serviceMapper);
    }

    @Test
    @DisplayName("bookService 成功预约应返回用户信息")
    void bookService_success_shouldReturnUser() {
        User user = new User();
        user.setId(1L);
        user.setName("张三");

        Services service = new Services();
        service.setServiceId(1);
        service.setServiceName("自习室预约");
        service.setServiceState(1);

        when(serviceMapper.selectByPrimaryKey(1L)).thenReturn(service);
        when(userMapper.selectByPrimaryKey(1L)).thenReturn(user);

        User result = bookService.bookService(1L, List.of(1));

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("张三", result.getName());
        verify(itemMapper).insertServices(eq(1L), eq(List.of(1)));
    }

    @Test
    @DisplayName("bookService 空服务ID列表应抛出异常")
    void bookService_emptyServiceIds_shouldThrow() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookService.bookService(1L, List.of()));
        assertEquals("服务ID列表不能为空", exception.getMessage());
    }

    @Test
    @DisplayName("bookService null服务ID列表应抛出异常")
    void bookService_nullServiceIds_shouldThrow() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookService.bookService(1L, null));
        assertEquals("服务ID列表不能为空", exception.getMessage());
    }

    @Test
    @DisplayName("bookService 服务不存在应抛出异常")
    void bookService_serviceNotFound_shouldThrow() {
        when(serviceMapper.selectByPrimaryKey(1L)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookService.bookService(1L, List.of(1)));
        assertEquals("服务ID 1 不存在", exception.getMessage());
    }

    @Test
    @DisplayName("bookService 服务已禁用应抛出异常")
    void bookService_serviceDisabled_shouldThrow() {
        Services service = new Services();
        service.setServiceId(1);
        service.setServiceState(0);

        when(serviceMapper.selectByPrimaryKey(1L)).thenReturn(service);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookService.bookService(1L, List.of(1)));
        assertEquals("服务ID 1 已被禁用", exception.getMessage());
    }

    @Test
    @DisplayName("cancelBookings 空预约ID列表应抛出异常")
    void cancelBookings_emptyBookingIds_shouldThrow() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookService.cancelBookings(1L, List.of()));
        assertEquals("取消的预约ID列表不能为空", exception.getMessage());
    }

    @Test
    @DisplayName("cancelBookings 成功取消应返回true")
    void cancelBookings_success_shouldReturnTrue() {
        when(itemMapper.setBookingStatusByParts(1L, List.of(1L, 2L))).thenReturn(2);

        boolean result = bookService.cancelBookings(1L, List.of(1L, 2L));

        assertTrue(result);
        verify(itemMapper).setBookingStatusByParts(1L, List.of(1L, 2L));
    }

    @Test
    @DisplayName("cancelBookings 部分预约不属于用户应抛出异常")
    void cancelBookings_partialMismatch_shouldThrow() {
        when(itemMapper.setBookingStatusByParts(1L, List.of(1L, 2L))).thenReturn(1);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookService.cancelBookings(1L, List.of(1L, 2L)));
        assertEquals("部分预约不属于您或不存在，无法取消", exception.getMessage());
    }

    @Test
    @DisplayName("getAllBookings 应返回预约列表")
    void getAllBookings_shouldReturnBookings() {
        bookService.getAllBookings(1L);
        verify(userMapper).getAllBookings(1L);
    }
}
