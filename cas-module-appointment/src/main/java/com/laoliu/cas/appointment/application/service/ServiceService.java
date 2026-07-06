package com.laoliu.cas.appointment.application.service;

import com.laoliu.cas.appointment.domain.entity.Service;
import com.laoliu.cas.appointment.interfaces.dto.request.ServiceAddRequest;
import com.laoliu.cas.common.result.PageResult;

import java.util.List;
import java.util.Optional;

/**
 * 服务管理应用服务接口
 *
 * @author forever-king
 */
public interface ServiceService {

    List<Service> getAllServices();

    /**
     * 分页获取所有服务。
     */
    PageResult<Service> getAllServices(int page, int pageSize);

    List<Service> getAvailableServices();

    /**
     * 分页获取可预约服务。
     */
    PageResult<Service> getAvailableServices(int page, int pageSize);

    Optional<Service> getServiceById(Long id);

    boolean addService(ServiceAddRequest request);

    List<Service> selectUserServices(Long userId);

    /**
     * 分页获取用户的服务。
     */
    PageResult<Service> selectUserServices(Long userId, int page, int pageSize);
}
