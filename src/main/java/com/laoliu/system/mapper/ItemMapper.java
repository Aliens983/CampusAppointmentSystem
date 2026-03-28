package com.laoliu.system.mapper;

import com.laoliu.system.entity.Item;
import com.laoliu.system.entity.Service;

import java.util.List;

/**
* @author 25516
* @description 针对表【item】的数据库操作Mapper
* @createDate 2026-03-16 11:42:54
* @Entity com.laoliu.system.entity.Item
*/
public interface ItemMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Item record);

    int insertSelective(Item record);

    Item selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Item record);

    int updateByPrimaryKey(Item record);

    List<Service> selectUserServices(Long userId);

    void setBookingStatus(Long userId,Long bookingId);
}
