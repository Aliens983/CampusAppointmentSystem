package com.laoliu.system.service;

import com.laoliu.system.entity.User;

import java.util.List;

/**
 * @author 25516
 */
public interface BookService {
    User bookService(Long userId,Integer serviceId);

    List<String> getAllBookings(Long userId);
}
