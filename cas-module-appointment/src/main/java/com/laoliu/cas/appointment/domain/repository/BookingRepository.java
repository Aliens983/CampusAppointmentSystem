package com.laoliu.cas.appointment.domain.repository;

import com.laoliu.cas.appointment.interfaces.dto.response.ServiceStatusResponse;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 预约/订单仓储接口
 *
 * @author forever-king
 */
public interface BookingRepository {

    void insertServices(Long userId, List<Integer> serviceIds);

    int cancelBookings(Long userId, List<Long> bookingIds);

    List<ServiceStatusResponse> getServiceStatus();

    /**
     * 分页查询所有服务预约状态（支持按审核状态、服务名称筛选）。
     */
    IPage<ServiceStatusResponse> getServiceStatus(int page, int pageSize, Integer manageStatus, String serviceName);

    List<ServiceStatusResponse> getServiceStatusByUserId(Long userId);

    /**
     * 分页查询用户的预约状态（支持按审核状态、服务名称筛选）。
     */
    IPage<ServiceStatusResponse> getServiceStatusByUserId(Long userId, int page, int pageSize, Integer manageStatus, String serviceName);

    ServiceStatusResponse getServiceStatusByOrderId(Long orderId);

    boolean auditService(Long orderId, Integer status, String reason);

    String getUserEmailByOrderId(Long orderId);
}
