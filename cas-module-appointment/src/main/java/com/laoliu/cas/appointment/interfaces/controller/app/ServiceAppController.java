package com.laoliu.cas.appointment.interfaces.controller.app;

import com.laoliu.cas.appointment.application.service.ServiceService;
import com.laoliu.cas.appointment.domain.entity.Service;
import com.laoliu.cas.common.result.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户端服务查询接口。
 *
 * @author forever-king
 */
@Tag(name = "服务查询（用户）")
@RestController
@RequestMapping("/app/service")
@RequiredArgsConstructor
public class ServiceAppController {

    private final ServiceService serviceService;

    @Operation(summary = "用户端：获取可预约服务", description = "用户端查询所有启用状态的服务")
    @GetMapping
    public CommonResult<List<Service>> getEnabledServices() {
        List<Service> services = serviceService.getAllServices().stream()
                .filter(Service::isAvailable)
                .toList();
        return CommonResult.success(services);
    }
}
