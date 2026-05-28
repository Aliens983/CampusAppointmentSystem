package com.laoliu.cas.appointment.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "服务添加请求类")
public class ServiceAddRequest {

    @Schema(description = "服务名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String serviceName;

    @Schema(description = "服务描述", requiredMode = Schema.RequiredMode.REQUIRED)
    private String serviceDescribe;

    @Schema(description = "服务状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer serviceState;
}
