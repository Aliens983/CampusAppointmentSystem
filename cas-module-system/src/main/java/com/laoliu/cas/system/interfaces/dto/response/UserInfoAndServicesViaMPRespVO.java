package com.laoliu.cas.system.interfaces.dto.response;

import com.laoliu.cas.common.domain.entity.User;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfoAndServicesViaMPRespVO implements Serializable {

    private User user;
}
