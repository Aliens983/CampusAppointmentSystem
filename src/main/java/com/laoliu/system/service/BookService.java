package com.laoliu.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.laoliu.system.entity.Services;
import com.laoliu.system.entity.User;
import com.laoliu.system.vo.response.UserInfoAndServicesViaMPRespVO;

import java.util.List;
import java.util.Map;

/**
 * @author 25516
 */
public interface BookService extends IService<Services> {
    User bookService(Long userId,List<Integer> serviceId);

    List<Map<String, Object>> getAllBookings(Long userId);

    boolean cancelBookings(Long userId,List<Long> bookingIds);

}
