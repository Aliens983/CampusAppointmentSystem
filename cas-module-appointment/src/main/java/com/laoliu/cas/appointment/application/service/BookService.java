package com.laoliu.cas.appointment.application.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.laoliu.cas.appointment.infrastructure.persistence.dataobject.ServicesDO;
import com.laoliu.cas.common.domain.entity.User;

import java.util.List;
import java.util.Map;

public interface BookService extends IService<ServicesDO> {

    User bookService(Long userId, List<Integer> serviceId);

    List<Map<String, Object>> getAllBookings(Long userId);

    boolean cancelBookings(Long userId, List<Long> bookingIds);
}
