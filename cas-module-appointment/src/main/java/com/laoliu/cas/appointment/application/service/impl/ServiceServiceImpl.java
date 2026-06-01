package com.laoliu.cas.appointment.application.service.impl;

import com.laoliu.cas.appointment.application.service.ServiceService;
import com.laoliu.cas.appointment.infrastructure.persistence.dataobject.ServicesDO;
import com.laoliu.cas.appointment.infrastructure.persistence.mapper.ItemMapper;
import com.laoliu.cas.appointment.infrastructure.persistence.mapper.ServiceMapper;
import com.laoliu.cas.appointment.interfaces.dto.request.ServiceAddRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author forever-king
 */
@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final ServiceMapper serviceMapper;
    private final ItemMapper itemMapper;

    @Override
    public List<ServicesDO> getAllServices() {
        return serviceMapper.selectAll();
    }

    @Override
    public boolean addService(ServiceAddRequest request) {
        ServicesDO servicesDO = new ServicesDO();
        servicesDO.setServiceName(request.getServiceName());
        servicesDO.setServiceDescribe(request.getServiceDescribe());
        servicesDO.setServiceState(request.getServiceState());
        return serviceMapper.insertSelective(servicesDO) > 0;
    }

    @Override
    public List<ServicesDO> selectUserServices(Long userId) {
        return serviceMapper.selectUserServices(userId);
    }
}
