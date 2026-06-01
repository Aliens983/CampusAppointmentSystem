package com.laoliu.cas.system.interfaces.dto.response;

import com.laoliu.cas.common.domain.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.Data;


/**
 * @author forever-king
 */
@Data
@Schema(description = "用户信息及预约服务响应")
public class UserInfoAndServicesViaMPRespVO implements Serializable {

    @Schema(description = "用户基本信息")
    private User user;

    @Schema(description = "用户预约的服务列表")
    private List<Map<String, Object>> bookings;
}
