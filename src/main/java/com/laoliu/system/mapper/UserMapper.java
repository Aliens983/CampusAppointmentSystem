package com.laoliu.system.mapper;

import com.laoliu.system.entity.User;

/**
* @author 25516
* @description 针对表【user】的数据库操作Mapper
* @createDate 2026-02-25 22:37:58
* @Entity com.laoliu.system.entity.User
*/
public interface UserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

}
