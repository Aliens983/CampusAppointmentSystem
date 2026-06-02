package com.laoliu.cas.system.api;

import com.laoliu.cas.system.domain.entity.User;

/**
 * @author forever-king
 */
public interface UserInfoApi {

    User getUserById(Long userId);

}
