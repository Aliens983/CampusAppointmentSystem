package com.laoliu.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.laoliu.system.entity.Services;
import com.laoliu.system.exception.BusinessException;
import com.laoliu.system.mapper.ServiceMapper;
import com.laoliu.system.service.impl.ServicesServiceImpl;
import com.laoliu.system.vo.request.ServiceAddRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicesServiceImplTest {

    @Mock
    private ServiceMapper serviceMapper;

    private ServicesServiceImpl servicesService;

    @BeforeEach
    void setUp() {
        servicesService = new ServicesServiceImpl(serviceMapper);
    }

    @Test
    @DisplayName("getEnabledServices 应只返回启用状态的服务")
    void getEnabledServices_shouldReturnOnlyEnabled() {
        Services s1 = new Services();
        s1.setServiceId(1);
        s1.setServiceState(1);
        Services s2 = new Services();
        s2.setServiceId(2);
        s2.setServiceState(0);
        Services s3 = new Services();
        s3.setServiceId(3);
        s3.setServiceState(1);

        when(serviceMapper.selectAll()).thenReturn(List.of(s1, s2, s3));

        List<Services> result = servicesService.getEnabledServices();

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(s -> s.getServiceState() == 1));
        verify(serviceMapper).selectAll();
    }

    @Test
    @DisplayName("getEnabledServices 所有服务都禁用时应返回空列表")
    void getEnabledServices_allDisabled_shouldReturnEmpty() {
        Services s1 = new Services();
        s1.setServiceState(0);
        Services s2 = new Services();
        s2.setServiceState(0);

        when(serviceMapper.selectAll()).thenReturn(List.of(s1, s2));

        List<Services> result = servicesService.getEnabledServices();

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getServicesByPage pageNum小于1应抛出异常")
    void getServicesByPage_invalidPageNum_shouldThrow() {
        assertThrows(BusinessException.class,
                () -> servicesService.getServicesByPage(0, 10, null));
    }

    @Test
    @DisplayName("getServicesByPage pageSize小于1应抛出异常")
    void getServicesByPage_invalidPageSize_shouldThrow() {
        assertThrows(BusinessException.class,
                () -> servicesService.getServicesByPage(1, 0, null));
    }

    @Test
    @DisplayName("getServicesByPage serviceState过滤应生效")
    void getServicesByPage_withServiceState_shouldFilter() {
        IPage<Services> mockPage = mock(IPage.class);
        when(mockPage.getPages()).thenReturn(1L);
        when(mockPage.getTotal()).thenReturn(1L);
        when(serviceMapper.selectPage(any(), any())).thenReturn(mockPage);

        servicesService.getServicesByPage(1, 10, 1);
        verify(serviceMapper).selectPage(any(), any());
    }

    @Test
    @DisplayName("addService 应返回成功状态")
    void addService_shouldReturnSuccess() {
        ServiceAddRequest request = new ServiceAddRequest();
        request.setServiceName("新服务");
        request.setServiceDescribe("服务描述");

        when(serviceMapper.insertSelective(request)).thenReturn(1);

        boolean result = servicesService.addService(request);

        assertTrue(result);
        verify(serviceMapper).insertSelective(request);
    }

    @Test
    @DisplayName("addService 插入失败应返回false")
    void addService_insertFailed_shouldReturnFalse() {
        ServiceAddRequest request = new ServiceAddRequest();
        when(serviceMapper.insertSelective(request)).thenReturn(0);

        boolean result = servicesService.addService(request);

        assertFalse(result);
    }
}
