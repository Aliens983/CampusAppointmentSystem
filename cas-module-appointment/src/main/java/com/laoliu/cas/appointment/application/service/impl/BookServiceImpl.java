package com.laoliu.cas.appointment.application.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.laoliu.cas.appointment.application.service.BookService;
import com.laoliu.cas.appointment.infrastructure.persistence.dataobject.ServicesDO;
import com.laoliu.cas.appointment.infrastructure.persistence.mapper.ItemMapper;
import com.laoliu.cas.appointment.infrastructure.persistence.mapper.ServiceMapper;
import com.laoliu.cas.system.api.UserInfoApi;
import com.laoliu.cas.system.domain.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author forever-king
 */
@Service
public class BookServiceImpl extends ServiceImpl<ServiceMapper, ServicesDO> implements BookService {

    private final ItemMapper itemMapper;
    private final ServiceMapper serviceMapper;
    private final UserInfoApi userInfoApi;

    public BookServiceImpl(ItemMapper itemMapper, ServiceMapper serviceMapper, UserInfoApi userInfoApi) {
        this.itemMapper = itemMapper;
        this.serviceMapper = serviceMapper;
        this.userInfoApi = userInfoApi;
    }

    @Override
    @Transactional
    public User bookService(Long userId, List<Integer> serviceId) {
        if (serviceId == null || serviceId.isEmpty()) {
            throw new RuntimeException("服务ID列表不能为空");
        }

        for (Integer sid : serviceId) {
            ServicesDO services = serviceMapper.selectByPrimaryKey(Long.valueOf(sid));
            if (services == null) {
                throw new RuntimeException("服务ID " + sid + " 不存在");
            }
            if (services.getServiceState() != 1) {
                throw new RuntimeException("服务ID " + sid + " 已被禁用");
            }
        }

        try {
            itemMapper.insertServices(userId, serviceId);
            return userInfoApi.getUserById(userId);
        } catch (Exception e) {
            throw new RuntimeException("预约失败: " + e.getCause(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getAllBookings(Long userId) {
        return itemMapper.getServiceStatusByUserId(userId).stream()
                .map(status -> {
                    Map<String, Object> map = new java.util.HashMap<>();
                    map.put("orderId", status.getOrderId());
                    map.put("userId", status.getUserId());
                    map.put("serviceName", status.getServiceName());
                    map.put("status", status.getManageStatus());
                    map.put("createTime", status.getCreateTime());
                    map.put("reason", status.getStatusDescription());
                    return map;
                })
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional
    public boolean cancelBookings(Long userId, List<Long> bookingIds) {
        if (bookingIds == null || bookingIds.isEmpty()) {
            return false;
        }
        return itemMapper.setBookingStatusByParts(userId, bookingIds) > 0;
    }
}
