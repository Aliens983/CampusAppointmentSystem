package com.laoliu.cas.system.application.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.laoliu.cas.system.domain.entity.User;
import com.laoliu.cas.system.interfaces.dto.response.UserInfoAndServicesViaMPRespVO;

/**
 * @author forever-king
 */
public interface UserService extends IService<User> {
    UserInfoAndServicesViaMPRespVO getUserInfoAndBookings(Long userId);
}
