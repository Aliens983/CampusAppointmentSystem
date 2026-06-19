package com.laoliu.cas.appointment.application.service;

import com.laoliu.cas.appointment.domain.entity.Service;
import com.laoliu.cas.appointment.interfaces.dto.request.ServiceAddRequest;

import java.util.List;

/**
 * 服务管理应用服务接口
 *
 * @author forever-king
 */
public interface ServiceService {

    List<Service> getAllServices();

    boolean addService(ServiceAddRequest request);

    List<Service> selectUserServices(Long userId);
}
