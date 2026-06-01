package com.laoliu.cas.common.api;

import com.laoliu.cas.common.domain.entity.User;

/**
 * @author forever-king
 */
public interface UserInfoApi {
    User getUserById(Long userId);
}
