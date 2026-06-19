package com.laoliu.cas.appointment.infrastructure.persistence.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 服务数据对象 - 用于 MyBatis-Plus ORM
 * 
 * @author forever-king
 */
@Data
@TableName("services")
public class ServicesDO {

    @TableId(type = IdType.AUTO)
    private Long serviceId;

    private String serviceName;

    private String serviceDescribe;

    private Integer serviceState;

    /**
     * 转换为领域实体
     */
    public com.laoliu.cas.appointment.domain.entity.Service toEntity() {
        return new com.laoliu.cas.appointment.domain.entity.Service(
                serviceId, serviceName, serviceDescribe, serviceState);
    }

    /**
     * 从领域实体创建
     */
    public static ServicesDO fromEntity(com.laoliu.cas.appointment.domain.entity.Service entity) {
        if (entity == null) {
            return null;
        }
        ServicesDO dataObject = new ServicesDO();
        dataObject.setServiceId(entity.getServiceId());
        dataObject.setServiceName(entity.getServiceName());
        dataObject.setServiceDescribe(entity.getServiceDescribe());
        dataObject.setServiceState(entity.getServiceState());
        return dataObject;
    }
}
