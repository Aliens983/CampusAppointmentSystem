package com.laoliu.cas.system.api.impl;

import com.laoliu.cas.common.api.UserInfoApi;
import com.laoliu.cas.common.domain.entity.User;
import com.laoliu.cas.system.infrastructure.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author forever-king
 */
@Service
@RequiredArgsConstructor
public class UserInfoApiImpl implements UserInfoApi {

    private final UserMapper userMapper;

    @Override
    public User getUserById(Long userId) {
        return userMapper.selectByPrimaryKey(userId);
    }
}
