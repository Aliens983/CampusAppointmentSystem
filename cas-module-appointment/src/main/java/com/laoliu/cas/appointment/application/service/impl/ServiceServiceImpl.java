package com.laoliu.cas.appointment.application.service.impl;

import com.laoliu.cas.appointment.application.service.ServiceService;
import com.laoliu.cas.appointment.domain.entity.Service;
import com.laoliu.cas.appointment.domain.repository.ServiceRepository;
import com.laoliu.cas.appointment.interfaces.dto.request.ServiceAddRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 服务管理应用服务实现
 *
 * @author forever-king
 */
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;

    @Override
    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    @Override
    public boolean addService(ServiceAddRequest request) {
        Service service = Service.builder()
                .serviceName(request.getServiceName())
                .serviceDescribe(request.getServiceDescribe())
                .serviceState(request.getServiceState())
                .build();
        serviceRepository.save(service);
        return true;
    }

    @Override
    public List<Service> selectUserServices(Long userId) {
        return serviceRepository.findByUserId(userId);
    }
}
