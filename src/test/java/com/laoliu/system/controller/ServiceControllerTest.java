package com.laoliu.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.laoliu.system.entity.Services;
import com.laoliu.system.mapper.ItemMapper;
import com.laoliu.system.mapper.UserMapper;
import com.laoliu.system.service.ServicesService;
import com.laoliu.system.utils.JWTUtils;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ServiceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ServicesService servicesService;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JWTUtils jwtUtils;

    @InjectMocks
    private ServiceController serviceController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(serviceController).build();
    }

    @Test
    @DisplayName("获取所有服务应返回服务列表")
    void getService_shouldReturnServiceList() throws Exception {
        Services s1 = new Services();
        s1.setServiceId(1);
        s1.setServiceName("自习室预约");
        s1.setServiceState(1);

        when(servicesService.getEnabledServices()).thenReturn(List.of(s1));

        mockMvc.perform(get("/service"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].serviceName").value("自习室预约"));
    }

    @Test
    @DisplayName("分页获取服务应返回分页数据")
    void getServicesByPage_shouldReturnPage() throws Exception {
        mockMvc.perform(get("/service/page")
                        .param("pageNum", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(servicesService).getServicesByPage(1, 10, null);
    }

    @Test
    @DisplayName("分页获取服务带serviceState参数")
    void getServicesByPage_withState_shouldFilter() throws Exception {
        mockMvc.perform(get("/service/page")
                        .param("pageNum", "1")
                        .param("pageSize", "10")
                        .param("serviceState", "1"))
                .andExpect(status().isOk());

        verify(servicesService).getServicesByPage(1, 10, 1);
    }
}
