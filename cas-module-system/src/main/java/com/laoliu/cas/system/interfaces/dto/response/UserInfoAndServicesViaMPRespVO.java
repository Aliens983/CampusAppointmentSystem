package com.laoliu.cas.system.interfaces.dto.response;

import com.laoliu.cas.common.domain.entity.User;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class UserInfoAndServicesViaMPRespVO implements Serializable {

    private User user;

    private List<Map<String, Object>> bookings;
}
