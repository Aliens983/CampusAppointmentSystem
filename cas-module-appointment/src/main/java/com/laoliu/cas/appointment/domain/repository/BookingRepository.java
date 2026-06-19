package com.laoliu.cas.appointment.domain.repository;

import com.laoliu.cas.appointment.interfaces.dto.response.ServiceStatusResponse;

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

    List<ServiceStatusResponse> getServiceStatusByUserId(Long userId);

    ServiceStatusResponse getServiceStatusByOrderId(Long orderId);

    boolean auditService(Long orderId, Integer status, String reason);

    String getUserEmailByOrderId(Long orderId);
}
