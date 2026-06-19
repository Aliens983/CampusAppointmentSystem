package com.laoliu.cas.appointment.domain.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * 服务领域实体
 * 纯净，不依赖任何框架注解
 *
 * @author forever-king
 */
public class Service implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long serviceId;
    private String serviceName;
    private String serviceDescribe;
    private Integer serviceState;

    public Service() {
    }

    public Service(Long serviceId, String serviceName, String serviceDescribe, Integer serviceState) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceDescribe = serviceDescribe;
        this.serviceState = serviceState;
    }

    /**
     * 领域行为：检查服务是否可用
     */
    public boolean isAvailable() {
        return Objects.equals(this.serviceState, 1);
    }

    /**
     * 领域行为：禁用服务
     */
    public void disable() {
        this.serviceState = 0;
    }

    /**
     * 领域行为：启用服务
     */
    public void enable() {
        this.serviceState = 1;
    }

    // Getter 和 Setter

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDescribe() {
        return serviceDescribe;
    }

    public void setServiceDescribe(String serviceDescribe) {
        this.serviceDescribe = serviceDescribe;
    }

    public Integer getServiceState() {
        return serviceState;
    }

    public void setServiceState(Integer serviceState) {
        this.serviceState = serviceState;
    }

    @Override
    public String toString() {
        return "Service{" +
                "serviceId=" + serviceId +
                ", serviceName='" + serviceName + '\'' +
                ", serviceDescribe='" + serviceDescribe + '\'' +
                ", serviceState=" + serviceState +
                '}';
    }
}
