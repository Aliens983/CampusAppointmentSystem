package com.laoliu.system.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author 25516
 */
@Schema(description = "服务响应结果")
@Data
public class ServicesRespVO {

    @Schema(description = "服务名称")
    private String serviceName;

    @Schema(description = "服务描述")
    private String serviceDescribe;

    @Schema(description ="服务状态")
    private String serviceState;
}
