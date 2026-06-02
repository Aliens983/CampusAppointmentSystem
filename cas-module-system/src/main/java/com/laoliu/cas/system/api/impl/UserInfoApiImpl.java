package com.laoliu.cas.system.api.impl;

import com.laoliu.cas.system.api.UserInfoApi;
import com.laoliu.cas.system.domain.entity.User;
import com.laoliu.cas.system.infrastructure.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author forever-king
 */
@Component
@RequiredArgsConstructor
public class UserInfoApiImpl implements UserInfoApi {

    private final UserMapper userMapper;

    @Override
    public User getUserById(Long userId) {
        return userMapper.selectById(userId);
    }
}