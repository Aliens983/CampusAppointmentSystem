package com.laoliu.system.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author 25516
 */
@Schema(description = "服务添加请求类")
@Data
public class ServiceAddRequest {

    @Schema(description = "服务名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String serviceName;

    @Schema(description = "服务描述", requiredMode = Schema.RequiredMode.REQUIRED)
    private String serviceDescribe;

}
