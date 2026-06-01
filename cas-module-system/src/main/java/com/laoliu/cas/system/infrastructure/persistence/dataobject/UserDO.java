package com.laoliu.cas.system.infrastructure.persistence.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author forever-king
 */
@Data
@TableName("user")
public class UserDO implements Serializable {

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
