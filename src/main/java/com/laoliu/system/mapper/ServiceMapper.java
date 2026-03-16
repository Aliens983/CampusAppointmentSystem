package com.laoliu.system.mapper;

import com.laoliu.system.entity.Service;

import java.util.List;

/**
* @author 25516
* @description 针对表【service】的数据库操作Mapper
* @createDate 2026-03-16 10:31:25
* @Entity com.laoliu.system.entity.Service
*/
public interface ServiceMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Service record);

    int insertSelective(Service record);

    Service selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Service record);

    int updateByPrimaryKey(Service record);

    List<Service> selectAll();
}
