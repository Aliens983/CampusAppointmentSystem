package com.laoliu.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.laoliu.system.entity.Services;
import com.laoliu.system.vo.request.ServiceAddRequest;

import java.util.List;

/**
 * @author 25516
 */
public interface ServicesService extends IService<Services> {

    List<Services> getEnabledServices();

    IPage<Services> getServicesByPage(int pageNum, int pageSize, Integer serviceState);

    boolean addService(ServiceAddRequest request);
}
