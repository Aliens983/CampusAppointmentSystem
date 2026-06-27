package com.laoliu.cas.system.domain.entity;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户领域实体
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
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String grade;

    private String sex;

    private Integer age;

    private String email;

    private String password;

    private Integer role;
}
