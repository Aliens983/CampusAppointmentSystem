package com.laoliu.cas.infra.api.impl;

import com.laoliu.cas.common.api.GetUserIdViaTokenApi;
import com.laoliu.cas.security.util.SecurityFrameworkUtils;
import org.springframework.stereotype.Component;

@Component
public class GetUserIdViaTokenApiImpl implements GetUserIdViaTokenApi {

    @Override
    public Long getUserId() {
        return SecurityFrameworkUtils.getLoginUserId();
    }
}