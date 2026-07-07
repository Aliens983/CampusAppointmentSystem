package com.laoliu.cas.appointment.application.service;

import com.laoliu.cas.appointment.interfaces.dto.request.ServiceStatusPageReqVO;
import com.laoliu.cas.appointment.interfaces.dto.response.ServiceStatusResponse;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 服务预约状态应用服务接口
 *
 * @author forever-king
 */
public interface ServiceStatusService {

    List<ServiceStatusResponse> getServiceStatus();

    /**
     * 分页查询所有服务预约状态（支持筛选）。
     */
    IPage<ServiceStatusResponse> getServiceStatus(ServiceStatusPageReqVO reqVO);

    List<ServiceStatusResponse> getServiceStatusByUserId(Long userId);

    /**
     * 分页查询用户的预约状态（支持筛选）。
     */
    IPage<ServiceStatusResponse> getServiceStatusByUserId(Long userId, ServiceStatusPageReqVO reqVO);

    List<ServiceStatusResponse> getServiceStatusByUserIdWithDescription(Long userId);

    /**
     * 分页查询用户的预约状态（含状态描述，支持筛选）。
     */
    IPage<ServiceStatusResponse> getServiceStatusByUserIdWithDescription(Long userId, ServiceStatusPageReqVO reqVO);

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
