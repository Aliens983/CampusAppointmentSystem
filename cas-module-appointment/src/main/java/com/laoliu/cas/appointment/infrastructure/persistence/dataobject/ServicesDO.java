package com.laoliu.cas.appointment.infrastructure.persistence.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author forever-king
 */
@Data
@TableName("services")
public class ServicesDO {

    @TableId(type = IdType.AUTO)
    private Integer serviceId;

    private String serviceName;

    private String serviceDescribe;

    private Integer serviceState;
}
