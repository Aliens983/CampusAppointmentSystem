package com.laoliu.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.laoliu.system.common.exception.enums.ServiceErrorCode;
import com.laoliu.system.entity.Services;
import com.laoliu.system.exception.BusinessException;
import com.laoliu.system.mapper.ServiceMapper;
import com.laoliu.system.service.ServicesService;
import com.laoliu.system.vo.request.ServiceAddRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 25516
 */
@Service
public class ServicesServiceImpl extends ServiceImpl<ServiceMapper, Services> implements ServicesService {

    private final ServiceMapper serviceMapper;

    public ServicesServiceImpl(ServiceMapper serviceMapper) {
        this.serviceMapper = serviceMapper;
    }

    @Override
    public List<Services> getEnabledServices() {
        return serviceMapper.selectAll().stream()
                .filter(s -> s.getServiceState() == 1)
                .toList();
    }

    @Override
    public IPage<Services> getServicesByPage(int pageNum, int pageSize, Integer serviceState) {
        if (pageNum < 1) {
            throw new BusinessException(ServiceErrorCode.PAGE_OUT_OF_RANGE);
        }
        if (pageSize < 1) {
            throw new BusinessException(ServiceErrorCode.PAGE_OUT_OF_RANGE);
        }
        Page<Services> pageParam = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Services> wrapper = Wrappers.lambdaQuery();
        if (serviceState != null) {
            wrapper.eq(Services::getServiceState, serviceState);
        }
        wrapper.orderByAsc(Services::getServiceId);
        IPage<Services> result = serviceMapper.selectPage(pageParam, wrapper);
        if (result.getTotal() > 0 && pageNum > result.getPages()) {
            throw new BusinessException(ServiceErrorCode.PAGE_OUT_OF_RANGE);
        }
        return result;
    }

    @Override
    public boolean addService(ServiceAddRequest request) {
        return serviceMapper.insertSelective(request) > 0;
    }
}
