package com.laoliu.system.entity;

/**
 * 
 * @author 25516
 * @TableName services
 */
public class Services {
    /**
     *
     */
    private Integer serviceId;

    /**
     *
     */
    private String serviceName;

    /**
     *
     */
    private String serviceDescribe;

    /**
     *
     */
    private Integer serviceState;

    /**
     *
     */
    public Integer getServiceId() {
        return serviceId;
    }

    /**
     *
     */
    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    /**
     *
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     *
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     *
     */
    public String getServiceDescribe() {
        return serviceDescribe;
    }

    /**
     *
     */
    public void setServiceDescribe(String serviceDescribe) {
        this.serviceDescribe = serviceDescribe;
    }

    /**
     *
     */
    public Integer getServiceState() {
        return serviceState;
    }

    /**
     *
     */
    public void setServiceState(Integer serviceState) {
        this.serviceState = serviceState;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Services other = (Services) that;
        return (this.getServiceId() == null ? other.getServiceId() == null : this.getServiceId().equals(other.getServiceId()))
            && (this.getServiceName() == null ? other.getServiceName() == null : this.getServiceName().equals(other.getServiceName()))
            && (this.getServiceDescribe() == null ? other.getServiceDescribe() == null : this.getServiceDescribe().equals(other.getServiceDescribe()))
            && (this.getServiceState() == null ? other.getServiceState() == null : this.getServiceState().equals(other.getServiceState()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getServiceId() == null) ? 0 : getServiceId().hashCode());
        result = prime * result + ((getServiceName() == null) ? 0 : getServiceName().hashCode());
        result = prime * result + ((getServiceDescribe() == null) ? 0 : getServiceDescribe().hashCode());
        result = prime * result + ((getServiceState() == null) ? 0 : getServiceState().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", serviceId=" + serviceId +
                ", serviceName=" + serviceName +
                ", serviceDescribe=" + serviceDescribe +
                ", serviceState=" + serviceState +
                "]";
    }
}
