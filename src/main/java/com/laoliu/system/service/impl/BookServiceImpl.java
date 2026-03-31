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
import java.util.Map;

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
    public User bookService(Long userId, List<Integer> serviceId) {

        //itemMapper.insert(new Item(null,userId,serviceId,new Date(),new Date()));
        // this maybe has the null pointer problem!!!
//        user.getServices().add(serviceMapper.selectByPrimaryKey(Long.valueOf(serviceId)));
        // suggestion: don't select the service from a database, just add it to the user's services
        // 仅仅把相关的条目加入到数据库, 不需要再把相关的服务查询出来展示在前端
//        List<com.laoliu.system.entity.Service> services = user.getServices();
//        services.add(serviceMapper.selectByPrimaryKey(Long.valueOf(serviceId)));
//        user.setServices(services);

        if (serviceId == null || serviceId.isEmpty()) {
            throw new RuntimeException("服务ID列表不能为空");
        }

        // 验证所有服务ID是否有效
        for (Integer sid : serviceId) {
            com.laoliu.system.entity.Service service = serviceMapper.selectByPrimaryKey(Long.valueOf(sid));
            if (service == null) {
                throw new RuntimeException("服务ID " + sid + " 不存在");
            }
            if (service.getServiceState() != 1) {
                throw new RuntimeException("服务ID " + sid + " 已被禁用");
            }
        }

        try {
            itemMapper.insertServices(userId, serviceId);
            return userMapper.selectByPrimaryKey(userId);
        } catch (Exception e) {
            throw new RuntimeException("预约失败: " + e.getCause(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getAllBookings(Long userId) {
        return userMapper.getAllBookings(userId);
    }

    @Override
    public boolean cancelBookings(Long userId, List<Long> bookingIds) {
        //直接把list传递进入性能会更好一点
        try {
//            for (Long bookingId : bookingIds) {
//                itemMapper.setBookingStatus(userId,bookingId);
//            }
            itemMapper.setBookingStatusByParts(userId, bookingIds);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
