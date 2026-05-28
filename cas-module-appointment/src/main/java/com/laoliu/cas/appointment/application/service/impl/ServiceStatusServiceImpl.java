package com.laoliu.cas.appointment.application.service.impl;

import com.laoliu.cas.appointment.application.service.ServiceStatusService;
import com.laoliu.cas.appointment.infrastructure.persistence.mapper.ItemMapper;
import com.laoliu.cas.appointment.interfaces.dto.response.ServiceStatusResponse;
import com.laoliu.cas.common.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceStatusServiceImpl implements ServiceStatusService {

    private final ItemMapper itemMapper;
    private final EmailService emailService;

    @Override
    public List<ServiceStatusResponse> getServiceStatus() {
        return itemMapper.getServiceStatus();
    }

    @Override
    public List<ServiceStatusResponse> getServiceStatusByUserId(Long userId) {
        return itemMapper.getServiceStatusByUserId(userId);
    }

    @Override
    public boolean auditService(Long orderId, Integer status, String reason) {
        int rows = itemMapper.auditService(orderId, status, reason);
        return rows > 0;
    }

    @Override
    public ServiceStatusResponse getServiceStatusByOrderId(Long orderId) {
        return itemMapper.getServiceStatusByOrderId(orderId);
    }

    @Override
    public void sendAuditEmail(Long orderId, String title, String content) {
        String email = itemMapper.getUserEmailByOrderId(orderId);
        if (email != null && !email.isEmpty()) {
            emailService.sendEmail(email, title, content);
        }
    }
}
