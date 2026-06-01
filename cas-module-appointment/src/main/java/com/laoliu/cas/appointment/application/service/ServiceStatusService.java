package com.laoliu.cas.appointment.application.service;

import com.laoliu.cas.appointment.interfaces.dto.response.ServiceStatusResponse;

import java.util.List;

/**
 * @author forever-king
 */
public interface ServiceStatusService {

    List<ServiceStatusResponse> getServiceStatus();

    List<ServiceStatusResponse> getServiceStatusByUserId(Long userId);

    boolean auditService(Long orderId, Integer status, String reason);

    ServiceStatusResponse getServiceStatusByOrderId(Long orderId);

    void sendAuditEmail(Long orderId, String title, String content);
}
