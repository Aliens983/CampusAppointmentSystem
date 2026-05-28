package com.laoliu.cas.system.api.impl;

import com.laoliu.cas.security.util.JWTUtils;
import com.laoliu.cas.system.api.GetUserIdViaTokenApi;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUserIdViaTokenApiImpl implements GetUserIdViaTokenApi {

    private final JWTUtils jwtUtils;

    @Override
    public Long getUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtUtils.getUserIdFromToken(token);
    }
}
