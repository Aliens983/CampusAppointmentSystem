package com.laoliu.cas.system.application.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.laoliu.cas.common.domain.entity.User;
import com.laoliu.cas.system.application.service.UserService;
import com.laoliu.cas.system.infrastructure.persistence.mapper.UserMapper;
import com.laoliu.cas.system.interfaces.dto.response.UserInfoAndServicesViaMPRespVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public UserInfoAndServicesViaMPRespVO getUserInfoAndBookings(Long userId) {
        User user = this.getById(userId);
        UserInfoAndServicesViaMPRespVO respVO = new UserInfoAndServicesViaMPRespVO();
        respVO.setUser(user);
        return respVO;
    }
}
