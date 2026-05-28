package com.laoliu.cas.common.api;

import com.laoliu.cas.common.domain.entity.User;

public interface UserInfoApi {
    User getUserById(Long userId);
}
