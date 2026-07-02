package com.laoliu.cas.appointment.application.service.impl;

import com.laoliu.cas.appointment.application.service.ConsultationService;
import com.laoliu.cas.appointment.domain.entity.Service;
import com.laoliu.cas.appointment.domain.repository.ServiceRepository;
import com.laoliu.cas.appointment.interfaces.dto.response.ConsultantResponse;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 咨询查询应用服务实现
 *
 * @author forever-king
 */
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ConsultationServiceImpl implements ConsultationService {

    private final ServiceRepository serviceRepository;

    private static final List<String> CONSULTATION_KEYWORDS = Arrays.asList(
            "咨询", "辅导", "指导", "心理"
    );

    private static final String[] TIME_SLOTS = {
            "09:00-10:00", "10:00-11:00", "11:00-12:00",
            "14:00-15:00", "15:00-16:00", "16:00-17:00"
    };

    @Override
    public List<ConsultantResponse> getAvailableConsultants() {
        return serviceRepository.findAll().stream()
                .filter(Service::isAvailable)
                .filter(this::isConsultation)
                .map(this::toConsultantResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ConsultantResponse getConsultantById(Long id) {
        Service service = serviceRepository.findById(id)
                .filter(Service::isAvailable)
                .orElse(null);
        if (service == null) {
            return null;
        }
        return toConsultantResponse(service);
    }

    @Override
    public List<Map<String, String>> getAvailableTimeSlots(Long consultantId, String date) {
        List<Map<String, String>> slots = new ArrayList<>();
        for (String time : TIME_SLOTS) {
            Map<String, String> slot = new LinkedHashMap<>();
            slot.put("time", time);
            slot.put("date", date);
            slot.put("available", "true");
            slots.add(slot);
        }
        return slots;
    }

    private boolean isConsultation(Service service) {
        if (service.getServiceName() == null) {
            return false;
        }
        return CONSULTATION_KEYWORDS.stream()
                .anyMatch(keyword -> service.getServiceName().contains(keyword));
    }

    private ConsultantResponse toConsultantResponse(Service service) {
        return ConsultantResponse.builder()
                .id(service.getServiceId())
                .name(service.getServiceName() + "老师")
                .title("资深顾问")
                .department("学生咨询中心")
                .expertise(Arrays.asList("学业指导", "心理咨询"))
                .rating(4.8)
                .reviews(128)
                .available(service.isAvailable())
                .nextSlot("今日 14:30")
                .avatar("")
                .build();
    }
}
