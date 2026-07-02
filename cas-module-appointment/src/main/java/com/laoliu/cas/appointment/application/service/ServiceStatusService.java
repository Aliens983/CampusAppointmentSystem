package com.laoliu.cas.appointment.application.service;

import com.laoliu.cas.appointment.interfaces.dto.response.ServiceStatusResponse;

import java.util.List;

/**
 * @author forever-king
 */
public interface ServiceStatusService {

    List<ServiceStatusResponse> getServiceStatus();

    List<ServiceStatusResponse> getServiceStatusByUserId(Long userId);

    List<ServiceStatusResponse> getServiceStatusByUserIdWithDescription(Long userId);

    boolean auditService(Long orderId, Integer status, String reason);

    ServiceStatusResponse getServiceStatusByOrderId(Long orderId);

    void sendAuditEmail(Long orderId, String title, String content);

    /**
     * 审核通过预约，发送通知邮件。
     *
     * @param orderId 订单ID
     * @param reason  审核备注（可选）
     */
    void auditPass(Long orderId, String reason);

    /**
     * 审核驳回预约，发送通知邮件。
     *
     * @param orderId 订单ID
     * @param reason  驳回原因（必填）
     */
    void auditReject(Long orderId, String reason);
}
