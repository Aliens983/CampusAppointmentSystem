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
@TableName("appointment_record")
@EqualsAndHashCode
public class AppointmentRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer recordId;

    private Integer itemId;

    private Integer userId;

    private String appointmentTime;

    private String appointmentPlace;

    private Integer appointmentState;

    private String description;

    private String createTime;

}
