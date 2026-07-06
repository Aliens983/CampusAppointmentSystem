package com.laoliu.cas.system.application.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.laoliu.cas.common.result.PageResult;
import com.laoliu.cas.system.application.service.UserService;
import com.laoliu.cas.system.domain.entity.User;
import com.laoliu.cas.system.domain.repository.UserRepository;
import com.laoliu.cas.system.interfaces.dto.response.UserInfoAndServicesViaMPRespVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author forever-king
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserInfoAndServicesViaMPRespVO getUserInfoAndBookings(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        UserInfoAndServicesViaMPRespVO respVO = new UserInfoAndServicesViaMPRespVO();
        respVO.setUser(user);
        respVO.setBookings(userRepository.getAllBookings(userId));
        return respVO;
    }

    @Override
    public PageResult<Map<String, Object>> getUserBookings(Long userId, int page, int pageSize) {
        IPage<Map<String, Object>> bookingsPage = userRepository.getAllBookings(userId, page, pageSize);
        return PageResult.of(bookingsPage);
    }
}
