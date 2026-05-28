package com.laoliu.cas.appointment.interfaces.controller.admin;

import com.laoliu.cas.appointment.application.service.ServicesService;
import com.laoliu.cas.common.domain.entity.Services;
import com.laoliu.cas.common.result.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "服务管理")
@RestController
@RequestMapping("/service")
@RequiredArgsConstructor
public class ServiceController {

    private final ServicesService servicesService;

    @Operation(summary = "获取所有服务")
    @GetMapping
    public CommonResult<List<Services>> getService() {
        try {
            List<Services> services = servicesService.getEnabledServices();
            return CommonResult.success(services);
        } catch (Exception e) {
            return CommonResult.internalServerError("获取服务失败：" + e.getMessage());
        }
    }
}
