package com.laoliu.system.service;

import com.laoliu.system.entity.User;

import java.util.List;
import java.util.Map;

/**
 * @author 25516
 */
public interface BookService {
    User bookService(Long userId,List<Integer> serviceId);

    List<Map<String, Object>> getAllBookings(Long userId);

    boolean cancelBookings(Long userId,List<Long> bookingIds);
}
