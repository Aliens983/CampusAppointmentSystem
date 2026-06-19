package com.laoliu.cas.system.infrastructure.persistence.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户数据对象 - 用于 MyBatis-Plus ORM
 *
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

    public com.laoliu.cas.system.domain.entity.User toEntity() {
        com.laoliu.cas.system.domain.entity.User entity = new com.laoliu.cas.system.domain.entity.User();
        entity.setId(id);
        entity.setName(name);
        entity.setGrade(grade);
        entity.setSex(sex);
        entity.setAge(age);
        entity.setEmail(email);
        entity.setPassword(password);
        entity.setRole(role);
        return entity;
    }

    public static UserDO fromEntity(com.laoliu.cas.system.domain.entity.User entity) {
        if (entity == null) return null;
        UserDO dataObject = new UserDO();
        dataObject.setId(entity.getId());
        dataObject.setName(entity.getName());
        dataObject.setGrade(entity.getGrade());
        dataObject.setSex(entity.getSex());
        dataObject.setAge(entity.getAge());
        dataObject.setEmail(entity.getEmail());
        dataObject.setPassword(entity.getPassword());
        dataObject.setRole(entity.getRole());
        return dataObject;
    }
}
