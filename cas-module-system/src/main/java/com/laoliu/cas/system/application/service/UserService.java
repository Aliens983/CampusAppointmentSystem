package com.laoliu.cas.system.application.service;

import com.laoliu.cas.system.interfaces.dto.response.UserInfoAndServicesViaMPRespVO;

/**
 * @author forever-king
 */
public interface UserService {
    UserInfoAndServicesViaMPRespVO getUserInfoAndBookings(Long userId);
}
