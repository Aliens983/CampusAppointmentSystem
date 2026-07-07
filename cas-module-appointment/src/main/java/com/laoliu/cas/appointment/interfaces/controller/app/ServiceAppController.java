package com.laoliu.cas.appointment.interfaces.controller.app;

import com.laoliu.cas.appointment.application.service.ServiceService;
import com.laoliu.cas.appointment.interfaces.convert.ServiceConvert;
import com.laoliu.cas.appointment.interfaces.dto.request.ServicePageReqVO;
import com.laoliu.cas.appointment.interfaces.dto.response.ServiceRespVO;
import com.laoliu.cas.common.result.CommonResult;
import com.laoliu.cas.common.result.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @Operation(summary = "用户端：获取可预约服务（分页+筛选）", description = "用户端分页查询服务，支持按名称模糊搜索和状态筛选")
    @GetMapping
    public CommonResult<PageResult<ServiceRespVO>> getEnabledServices(@Valid ServicePageReqVO reqVO) {
        return CommonResult.success(ServiceConvert.INSTANCE.convertPage(serviceService.getAllServices(reqVO)));
    }
}
