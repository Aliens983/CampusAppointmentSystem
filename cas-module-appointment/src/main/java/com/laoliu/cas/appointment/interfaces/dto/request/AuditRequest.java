package com.laoliu.cas.appointment.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author forever-king
 */
@Data
@Schema(description = "服务预约审核请求")
public class AuditRequest {

    @Schema(description = "订单ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long orderId;

    @Schema(description = "审核状态（1-通过，2-拒绝）", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;

    @Schema(description = "审核原因（审核状态为拒绝时必填）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String reason;
}
