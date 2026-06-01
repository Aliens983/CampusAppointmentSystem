package com.laoliu.cas.appointment.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author forever-king
 */
@Data
@Schema(description = "服务添加请求")
public class ServiceAddRequest {

    @Schema(description = "服务名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "图书馆预约")
    private String serviceName;

    @Schema(description = "服务描述", requiredMode = Schema.RequiredMode.REQUIRED, example = "预约图书馆座位或图书")
    private String serviceDescribe;

    @Schema(description = "服务状态（0-禁用，1-启用）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer serviceState;
}
