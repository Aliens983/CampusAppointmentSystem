package com.laoliu.cas.appointment.application.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.laoliu.cas.common.domain.entity.Services;
import com.laoliu.cas.appointment.application.service.ServicesService;
import com.laoliu.cas.appointment.infrastructure.persistence.mapper.ServicesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicesServiceImpl extends ServiceImpl<ServicesMapper, Services> implements ServicesService {

    @Override
    public List<Services> getEnabledServices() {
        return this.lambdaQuery()
                .eq(Services::getServiceState, 1)
                .list();
    }
}
