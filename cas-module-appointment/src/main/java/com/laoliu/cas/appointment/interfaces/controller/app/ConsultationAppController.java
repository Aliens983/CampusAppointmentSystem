package com.laoliu.cas.appointment.interfaces.controller.app;

import com.laoliu.cas.appointment.application.service.ConsultationService;
import com.laoliu.cas.appointment.interfaces.dto.response.ConsultantResponse;
import com.laoliu.cas.common.result.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 咨询查询接口。
 *
 * @author forever-king
 */
@Tag(name = "咨询查询")
@RestController
@RequestMapping("/consultation")
@RequiredArgsConstructor
public class ConsultationAppController {

    private final ConsultationService consultationService;

    @Operation(summary = "获取咨询师列表", description = "获取所有咨询类服务列表")
    @GetMapping
    public CommonResult<List<ConsultantResponse>> getConsultants() {
        return CommonResult.success(consultationService.getAvailableConsultants());
    }

    @Operation(summary = "获取咨询师详情", description = "根据ID获取单个咨询师详细信息")
    @GetMapping("/{id}")
    public CommonResult<ConsultantResponse> getConsultant(@PathVariable Long id) {
        ConsultantResponse consultant = consultationService.getConsultantById(id);
        if (consultant == null) {
            return CommonResult.notFound("咨询师不存在");
        }
        return CommonResult.success(consultant);
    }

    @Operation(summary = "获取可用时段", description = "获取指定咨询师的可用预约时段")
    @GetMapping("/available")
    public CommonResult<List<Map<String, String>>> getAvailableTime(
            @RequestParam Long consultantId,
            @RequestParam String date) {
        return CommonResult.success(consultationService.getAvailableTimeSlots(consultantId, date));
    }
}
