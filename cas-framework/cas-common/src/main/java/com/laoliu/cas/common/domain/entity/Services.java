package com.laoliu.cas.common.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author forever-king
 */
@Data
@TableName("services")
@EqualsAndHashCode
public class Services implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer serviceId;

    private String serviceName;

    private String serviceDescribe;

    private Integer serviceState;

}
