package com.laoliu.cas.appointment.application.service.impl;

import com.laoliu.cas.appointment.application.service.ConsultationService;
import com.laoliu.cas.appointment.domain.entity.Consultant;
import com.laoliu.cas.appointment.domain.entity.Service;
import com.laoliu.cas.appointment.domain.repository.ServiceRepository;
import com.laoliu.cas.appointment.infrastructure.persistence.mapper.ConsultantMapper;
import com.laoliu.cas.appointment.interfaces.dto.response.ConsultantResponse;
import com.laoliu.cas.appointment.interfaces.dto.response.TimeSlotRespVO;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 咨询查询应用服务实现 — 从数据库读取真实咨询师数据。
 *
 * @author forever-king
 */
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ConsultationServiceImpl implements ConsultationService {

    private final ServiceRepository serviceRepository;
    private final ConsultantMapper consultantMapper;

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
                .flatMap(service -> consultantMapper.findByServiceId(service.getServiceId()).stream())
                .map(doObj -> toConsultantResponse(doObj.toEntity()))
                .collect(Collectors.toList());
    }

    @Override
    public ConsultantResponse getConsultantById(Long id) {
        var consultantDO = consultantMapper.selectById(id);
        if (consultantDO == null) {
            return null;
        }
        return toConsultantResponse(consultantDO.toEntity());
    }

    @Override
    public List<TimeSlotRespVO> getAvailableTimeSlots(Long consultantId, String date) {
        List<TimeSlotRespVO> slots = new ArrayList<>();
        for (String time : TIME_SLOTS) {
            String[] parts = time.split("-");
            slots.add(TimeSlotRespVO.builder()
                    .startTime(parts[0].trim())
                    .endTime(parts.length > 1 ? parts[1].trim() : "")
                    .available("true")
                    .build());
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

    private ConsultantResponse toConsultantResponse(Consultant consultant) {
        return ConsultantResponse.builder()
                .id(consultant.getId())
                .name(consultant.getName())
                .title(consultant.getTitle())
                .department(consultant.getDepartment())
                .expertise(Collections.singletonList(consultant.getDescription()))
                .rating(consultant.getRating() != null ? consultant.getRating().doubleValue() : 5.0)
                .reviews(consultant.getReviewCount() != null ? consultant.getReviewCount() : 0)
                .available(true)
                .avatar(consultant.getAvatarUrl() != null ? consultant.getAvatarUrl() : "")
                .build();
    }
}
