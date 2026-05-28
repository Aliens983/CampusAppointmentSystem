package com.laoliu.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.laoliu.system.entity.Item;
import com.laoliu.system.entity.Services;
import com.laoliu.system.entity.User;
import com.laoliu.system.exception.ResourceNotFoundException;
import com.laoliu.system.mapper.UserMapper;
import com.laoliu.system.service.UserService;
import com.laoliu.system.vo.response.ServicesRespVO;
import com.laoliu.system.vo.response.UserInfoAndServicesViaMPRespVO;
import com.laoliu.system.vo.response.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 25516
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public UserInfoAndServicesViaMPRespVO getUserInfoAndBookings(Long userId) {
        // TODO: 这里我感觉还能做新能优化因为查询的速度是在是太慢了接口响应要2.5秒1坤秒了,too slow.
        // 查询用户
        User user = getById(userId);
        if (user == null){
            throw new ResourceNotFoundException("用户不存在");
        }

        // 查询用户预约服务的信息
        List<Item> list = Db.lambdaQuery(Item.class)
                .eq(Item::getUserId, userId)
                .list();

        LambdaQueryChainWrapper<Services> servicesLambdaQueryChainWrapper = Db.lambdaQuery(Services.class)
                .select(Services::getServiceName, Services::getServiceDescribe, Services::getServiceState)
                // not equal
                .ne(Services::getServiceState, 1)
                .in(Services::getServiceId, list.stream().map(Item::getServiceId).toArray());

        List<Services> services = servicesLambdaQueryChainWrapper.list();
        List<ServicesRespVO> servicesRespVO = BeanUtil.copyToList(services, ServicesRespVO.class);

        // when you use BeanUtil, You should add the dependency on pom.xml
        UserInfoAndServicesViaMPRespVO userInfoAndServicesViaMPRespVO = BeanUtil.copyProperties(user, UserInfoAndServicesViaMPRespVO.class);

        // 封装返回数据
        userInfoAndServicesViaMPRespVO.setServices(servicesRespVO);
        userInfoAndServicesViaMPRespVO.setUserResponse(BeanUtil.copyProperties(user, UserResponse.class));
        return userInfoAndServicesViaMPRespVO;


    }
}
