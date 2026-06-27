package com.laoliu.cas.appointment.domain.entity;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * 预约记录领域实体
 * 纯净，不依赖任何框架注解
 *
 * @author forever-king
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode()
@ToString
@Builder
public class AppointmentRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer recordId;

    private Integer itemId;

    private Long userId;

    private String appointmentTime;

    private String appointmentPlace;

    private Integer appointmentState;

    private String description;

    private String createTime;

}
