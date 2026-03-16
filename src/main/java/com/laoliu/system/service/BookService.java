package com.laoliu.system.service;

import com.laoliu.system.entity.User;

/**
 * @author 25516
 */
public interface BookService {
    User bookService(Long userId,Integer serviceId);
}
