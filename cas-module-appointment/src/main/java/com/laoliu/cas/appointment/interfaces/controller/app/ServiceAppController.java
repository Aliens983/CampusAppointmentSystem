package com.laoliu.cas.appointment.interfaces.controller.app;

import com.laoliu.cas.appointment.application.service.ServiceService;
import com.laoliu.cas.appointment.domain.entity.Service;
import com.laoliu.cas.common.result.CommonResult;
import com.laoliu.cas.common.result.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "用户端：获取可预约服务（分页）", description = "用户端分页查询所有启用状态的服务")
    @GetMapping
    public CommonResult<PageResult<Service>> getEnabledServices(
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int pageSize) {
        return CommonResult.success(serviceService.getAvailableServices(page, pageSize));
    }
}
