package com.laoliu.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.laoliu.system.entity.User;
import com.laoliu.system.vo.response.UserInfoAndServicesViaMPRespVO;

/**
 * @author 25516
 */
public interface UserService extends IService<User> {
    UserInfoAndServicesViaMPRespVO getUserInfoAndBookings(Long userId);
}
