package com.laoliu.cas.appointment.application.service.impl;

import com.laoliu.cas.appointment.application.service.ServiceStatusService;
import com.laoliu.cas.appointment.domain.repository.BookingRepository;
import com.laoliu.cas.appointment.interfaces.dto.response.ServiceStatusResponse;
import com.laoliu.cas.infra.application.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author forever-king
 */
@Service
@RequiredArgsConstructor
public class ServiceStatusServiceImpl implements ServiceStatusService {

    private final BookingRepository bookingRepository;
    private final EmailService emailService;

    @Override
    public List<ServiceStatusResponse> getServiceStatus() {
        return bookingRepository.getServiceStatus();
    }

    @Override
    public List<ServiceStatusResponse> getServiceStatusByUserId(Long userId) {
        return bookingRepository.getServiceStatusByUserId(userId);
    }

    @Override
    public List<ServiceStatusResponse> getServiceStatusByUserIdWithDescription(Long userId) {
        List<ServiceStatusResponse> statusList = bookingRepository.getServiceStatusByUserId(userId);
        statusList.forEach(this::setStatusDescription);
        return statusList;
    }

    @Override
    public boolean auditService(Long orderId, Integer status, String reason) {
        return bookingRepository.auditService(orderId, status, reason);
    }

    @Override
    public ServiceStatusResponse getServiceStatusByOrderId(Long orderId) {
        return bookingRepository.getServiceStatusByOrderId(orderId);
    }

    @Override
    public void sendAuditEmail(Long orderId, String title, String content) {
        String email = bookingRepository.getUserEmailByOrderId(orderId);
        if (email != null && !email.isEmpty()) {
            emailService.sendEmail(email, title, content);
        }
    }

    private void setStatusDescription(ServiceStatusResponse response) {
        if (response.getManageStatus() != null) {
            switch (response.getManageStatus()) {
                case 0 -> response.setStatusDescription("待审核");
                case 1 -> response.setStatusDescription("通过");
                case 2 -> response.setStatusDescription("拒绝");
                case 3 -> response.setStatusDescription("取消");
                default -> response.setStatusDescription("未知状态");
            }
        }
    }
}
