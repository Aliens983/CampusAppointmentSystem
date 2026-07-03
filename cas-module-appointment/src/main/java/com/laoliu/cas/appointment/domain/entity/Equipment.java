package com.laoliu.cas.appointment.domain.entity;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * 设备领域实体 — 纯净，不依赖框架注解。
 *
 * @author forever-king
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Equipment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String description;
    private Integer totalStock;
    private Integer availableStock;
    private String unit;
    private String location;
    private Long serviceId;

    public boolean isAvailable() {
        return availableStock != null && availableStock > 0;
    }
}
