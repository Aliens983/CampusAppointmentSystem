package com.laoliu.system.controller;

import com.laoliu.system.api.GetUserIdViaTokenApi;
import com.laoliu.system.entity.User;
import com.laoliu.system.service.BookService;
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

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @Mock
    private JWTUtils jwtUtils;

    @Mock
    private GetUserIdViaTokenApi getUserIdViaTokenApi;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    @DisplayName("预定服务成功")
    void bookService_success_shouldReturn200() throws Exception {
        String token = "Bearer test-token";
        Claims claims = mock(Claims.class);

        when(jwtUtils.parseToken("test-token")).thenReturn(claims);
        when(claims.getSubject()).thenReturn("1");

        User user = new User();
        user.setId(1L);
        user.setName("张三");
        user.setEmail("test@test.com");

        when(bookService.bookService(1L, List.of(1, 2))).thenReturn(user);
        when(bookService.getAllBookings(1L)).thenReturn(List.of(Map.of("serviceId", 1)));

        mockMvc.perform(post("/book")
                        .header("Authorization", token)
                        .param("serviceIds", "1", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value("张三"));
    }

    @Test
    @DisplayName("查看所有预约")
    void getBook_shouldReturnBookings() throws Exception {
        when(getUserIdViaTokenApi.getUserId(any())).thenReturn(1L);
        when(bookService.getAllBookings(1L)).thenReturn(List.of(Map.of("serviceName", "自习室预约")));

        mockMvc.perform(get("/book/allService")
                        .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @DisplayName("取消预约成功")
    void cancelBooking_success_shouldReturn200() throws Exception {
        when(getUserIdViaTokenApi.getUserId(any())).thenReturn(1L);
        when(bookService.cancelBookings(1L, List.of(1L, 2L))).thenReturn(true);

        mockMvc.perform(post("/book/cancel")
                        .header("Authorization", "Bearer test-token")
                        .param("bookingIds", "1", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }
}
