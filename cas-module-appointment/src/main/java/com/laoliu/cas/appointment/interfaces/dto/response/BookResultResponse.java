package com.laoliu.cas.appointment.interfaces.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * @author forever-king
 */
@Data
@Schema(description = "预约结果响应")
public class BookResultResponse {

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "用户年级")
    private String grade;

    @Schema(description = "用户预约的服务列表")
    private List<Map<String, Object>> allBookedServices;
}
