package com.laoliu.cas.system.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.laoliu.cas.common.domain.entity.User;
import com.laoliu.cas.system.infrastructure.persistence.dataobject.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM user WHERE id = #{id}")
    User selectByPrimaryKey(@Param("id") Long id);

    @Select("SELECT * FROM user WHERE email = #{email} LIMIT 1")
    Long getUserIdByEmail(@Param("email") String email);

    @Select("SELECT * FROM user")
    List<UserDO> getAllUsers();

    int insert(User user);
}
