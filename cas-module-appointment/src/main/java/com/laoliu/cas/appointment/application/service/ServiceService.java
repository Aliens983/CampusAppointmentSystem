package com.laoliu.cas.appointment.application.service;

import com.laoliu.cas.appointment.infrastructure.persistence.dataobject.ServicesDO;
import com.laoliu.cas.appointment.interfaces.dto.request.ServiceAddRequest;

import java.util.List;

/**
 * @author forever-king
 */
public interface ServiceService {

    List<ServicesDO> getAllServices();

    boolean addService(ServiceAddRequest request);

    List<ServicesDO> selectUserServices(Long userId);
}
