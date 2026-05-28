package com.laoliu.system.api;

import com.laoliu.system.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

/**
 * @author 25516
 */
@Service
public class GetUserIdViaTokenApiImpl implements GetUserIdViaTokenApi {

    private final JWTUtils jwtUtils;

    public GetUserIdViaTokenApiImpl(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Long getUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        // 移除 "Bearer " 前缀
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Claims claims = jwtUtils.parseToken(token);
        // 获取用户ID
        return Long.valueOf((claims.getSubject()));
    }
}
