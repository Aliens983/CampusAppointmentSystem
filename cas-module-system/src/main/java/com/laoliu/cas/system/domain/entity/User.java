package com.laoliu.cas.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author forever-king
 */
@Data
@TableName("user")
@ToString(exclude = {"password"})
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String grade;

    private String sex;

    private Integer age;

    private String email;

    private String password;

    private Integer role;

}
