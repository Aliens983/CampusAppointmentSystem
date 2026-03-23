package com.laoliu.system.service.impl;

import com.laoliu.system.entity.Item;
import com.laoliu.system.entity.User;
import com.laoliu.system.mapper.ItemMapper;
import com.laoliu.system.mapper.ServiceMapper;
import com.laoliu.system.mapper.UserMapper;
import com.laoliu.system.service.BookService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author 25516
 */
@Service
public class BookServiceImpl implements BookService {
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;
    private final ServiceMapper serviceMapper;

    public BookServiceImpl(ItemMapper itemMapper, UserMapper userMapper, ServiceMapper serviceMapper) {
        this.itemMapper = itemMapper;
        this.userMapper = userMapper;
        this.serviceMapper = serviceMapper;
    }

    @Override
    public User bookService(Long userId,Integer serviceId) {
        itemMapper.insert(new Item(null,userId,serviceId,new Date(),new Date()));
        User user = userMapper.selectByPrimaryKey(userId);
        // this maybe has the null pointer problem!!!
//        user.getServices().add(serviceMapper.selectByPrimaryKey(Long.valueOf(serviceId)));
        // suggestion: don't select the service from a database, just add it to the user's services
        // 仅仅把相关的条目加入到数据库, 不需要再把相关的服务查询出来展示在前端
//        List<com.laoliu.system.entity.Service> services = user.getServices();
//        services.add(serviceMapper.selectByPrimaryKey(Long.valueOf(serviceId)));
//        user.setServices(services);
        return user;
    }

    @Override
    public List<String> getAllBookings(Long userId) {
        return userMapper.getAllBookings(userId);
    }

}
