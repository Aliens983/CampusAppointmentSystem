package com.laoliu.cas.appointment.domain.entity;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 咨询师领域实体 — 纯净，不依赖框架注解。
 *
 * @author forever-king
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Consultant implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String department;
    private String title;
    private String description;
    private BigDecimal rating;
    private Integer reviewCount;
    private String avatarUrl;
    private Long serviceId;

    public boolean hasRatings() {
        return reviewCount != null && reviewCount > 0;
    }
}
