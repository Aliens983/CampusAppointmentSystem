package com.laoliu.cas.appointment.application.service.impl;

import com.laoliu.cas.appointment.application.service.EquipmentService;
import com.laoliu.cas.appointment.domain.entity.Equipment;
import com.laoliu.cas.appointment.domain.entity.Service;
import com.laoliu.cas.appointment.domain.repository.ServiceRepository;
import com.laoliu.cas.appointment.infrastructure.persistence.mapper.EquipmentMapper;
import com.laoliu.cas.appointment.interfaces.dto.response.EquipmentResponse;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备查询应用服务实现 — 从数据库读取真实设备数据。
 *
 * @author forever-king
 */
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {

    private final ServiceRepository serviceRepository;
    private final EquipmentMapper equipmentMapper;

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
                .flatMap(service -> equipmentMapper.findByServiceId(service.getServiceId()).stream())
                .map(doObj -> toEquipmentResponse(doObj.toEntity()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getCategories() {
        return CATEGORIES;
    }

    @Override
    public EquipmentResponse getEquipmentById(Long id) {
        var equipmentDO = equipmentMapper.selectById(id);
        if (equipmentDO == null) {
            return null;
        }
        return toEquipmentResponse(equipmentDO.toEntity());
    }

    private boolean isEquipment(Service service) {
        if (service.getServiceName() == null) {
            return false;
        }
        return EQUIPMENT_KEYWORDS.stream()
                .anyMatch(keyword -> service.getServiceName().contains(keyword));
    }

    private EquipmentResponse toEquipmentResponse(Equipment equipment) {
        return EquipmentResponse.builder()
                .id(equipment.getId())
                .name(equipment.getName())
                .category("设备资源")
                .description(equipment.getDescription())
                .stock(equipment.getTotalStock())
                .availableStock(equipment.getAvailableStock())
                .unit(equipment.getUnit())
                .priceLabel(equipment.isAvailable() ? "可借用" : "暂不可借")
                .location(equipment.getLocation())
                .image("gradient-teal")
                .build();
    }
}
