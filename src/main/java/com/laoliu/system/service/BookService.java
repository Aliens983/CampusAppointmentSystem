package com.laoliu.system.service;

import com.laoliu.system.entity.User;

import java.util.List;

/**
 * @author 25516
 */
public interface BookService {
    User bookService(Long userId,List<Integer> serviceId);

    List<String> getAllBookings(Long userId);

    boolean cancelBook(Long userId, List<Integer> serviceId);

    boolean changeStatus(Long userId, Integer serviceIds ,String status);
}
