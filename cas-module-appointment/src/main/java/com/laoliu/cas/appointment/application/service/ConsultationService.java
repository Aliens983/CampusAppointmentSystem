package com.laoliu.cas.appointment.application.service;

import com.laoliu.cas.appointment.interfaces.dto.response.ConsultantResponse;

import java.util.List;
import java.util.Map;

/**
 * 咨询查询应用服务接口
 *
 * @author forever-king
 */
public interface ConsultationService {

    /**
     * 获取所有可预约的咨询师列表
     */
    List<ConsultantResponse> getAvailableConsultants();

    /**
     * 根据ID获取咨询师详情
     */
    ConsultantResponse getConsultantById(Long id);

    /**
     * 获取指定咨询师的可用时段
     */
    List<Map<String, String>> getAvailableTimeSlots(Long consultantId, String date);
}
