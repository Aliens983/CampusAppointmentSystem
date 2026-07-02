package com.laoliu.cas.appointment.interfaces.controller.app;

import com.laoliu.cas.appointment.application.service.EquipmentService;
import com.laoliu.cas.appointment.interfaces.dto.response.EquipmentResponse;
import com.laoliu.cas.common.result.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备查询接口。
 *
 * @author forever-king
 */
@Tag(name = "设备查询")
@RestController
@RequestMapping("/equipment")
@RequiredArgsConstructor
public class EquipmentAppController {

    private final EquipmentService equipmentService;

    @Operation(summary = "获取设备列表", description = "获取所有设备类服务列表")
    @GetMapping
    public CommonResult<List<EquipmentResponse>> getEquipment() {
        return CommonResult.success(equipmentService.getAvailableEquipment());
    }

    @Operation(summary = "获取设备分类", description = "获取所有设备分类列表")
    @GetMapping("/categories")
    public CommonResult<List<String>> getCategories() {
        return CommonResult.success(equipmentService.getCategories());
    }

    @Operation(summary = "获取设备详情", description = "根据ID获取单个设备详细信息")
    @GetMapping("/{id}")
    public CommonResult<EquipmentResponse> getEquipmentDetail(@PathVariable Long id) {
        EquipmentResponse equipment = equipmentService.getEquipmentById(id);
        if (equipment == null) {
            return CommonResult.notFound("设备不存在");
        }
        return CommonResult.success(equipment);
    }
}
