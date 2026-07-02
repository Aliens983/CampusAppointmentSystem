package com.laoliu.cas.appointment.application.service.impl;

import com.laoliu.cas.appointment.application.service.EquipmentService;
import com.laoliu.cas.appointment.domain.entity.Service;
import com.laoliu.cas.appointment.domain.repository.ServiceRepository;
import com.laoliu.cas.appointment.interfaces.dto.response.EquipmentResponse;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备查询应用服务实现
 *
 * @author forever-king
 */
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {

    private final ServiceRepository serviceRepository;

    private static final List<String> EQUIPMENT_KEYWORDS = Arrays.asList(
            "设备", "投影", "相机", "仪器", "电脑", "笔记本", "打印机", "扫描仪"
    );

    private static final List<String> CATEGORIES = Arrays.asList(
            "投影设备", "音频设备", "摄影摄像", "实验仪器", "计算机设备", "其他设备"
    );

    @Override
    public List<EquipmentResponse> getAvailableEquipment() {
        return serviceRepository.findAll().stream()
                .filter(Service::isAvailable)
                .filter(this::isEquipment)
                .map(this::toEquipmentResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getCategories() {
        return CATEGORIES;
    }

    @Override
    public EquipmentResponse getEquipmentById(Long id) {
        Service service = serviceRepository.findById(id)
                .filter(Service::isAvailable)
                .orElse(null);
        if (service == null) {
            return null;
        }
        return toEquipmentResponse(service);
    }

    private boolean isEquipment(Service service) {
        if (service.getServiceName() == null) {
            return false;
        }
        return EQUIPMENT_KEYWORDS.stream()
                .anyMatch(keyword -> service.getServiceName().contains(keyword));
    }

    private EquipmentResponse toEquipmentResponse(Service service) {
        return EquipmentResponse.builder()
                .id(service.getServiceId())
                .name(service.getServiceName())
                .category("设备资源")
                .description(service.getServiceDescribe())
                .stock(10)
                .availableStock(service.isAvailable() ? 5 : 0)
                .unit("台")
                .priceLabel(service.isAvailable() ? "可借用" : "暂不可借")
                .location("校园设备管理中心")
                .image("gradient-teal")
                .build();
    }
}
