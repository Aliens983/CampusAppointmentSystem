package com.laoliu.cas.system.application.service;

import com.laoliu.cas.common.result.PageResult;
import com.laoliu.cas.system.interfaces.dto.response.UserInfoAndServicesViaMPRespVO;

import java.util.Map;

/**
 * @author forever-king
 */
public interface UserService {
    UserInfoAndServicesViaMPRespVO getUserInfoAndBookings(Long userId);

    /**
     * 分页获取用户的预约记录。
     */
    PageResult<Map<String, Object>> getUserBookings(Long userId, int page, int pageSize);
}
