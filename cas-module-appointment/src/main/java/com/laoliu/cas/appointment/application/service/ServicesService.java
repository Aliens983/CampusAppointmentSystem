package com.laoliu.cas.appointment.application.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.laoliu.cas.common.domain.entity.Services;

import java.util.List;

/**
 * @author forever-king
 */
public interface ServicesService extends IService<Services> {
    List<Services> getEnabledServices();
}
