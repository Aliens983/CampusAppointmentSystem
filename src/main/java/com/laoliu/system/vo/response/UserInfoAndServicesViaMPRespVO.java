package com.laoliu.system.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author 25516
 */
@Data
public class UserInfoAndServicesViaMPRespVO {

    @Schema(description = "用户信息")
    private UserResponse userResponse;


    private List<ServicesRespVO> services;

}
