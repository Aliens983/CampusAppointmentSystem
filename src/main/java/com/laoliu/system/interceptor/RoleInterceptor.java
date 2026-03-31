package com.laoliu.system.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laoliu.system.annotation.RequireRole;
import com.laoliu.system.api.GetUserIdViaTokenApi;
import com.laoliu.system.enums.UserRoleEnum;
import com.laoliu.system.mapper.UserMapper;
import com.laoliu.system.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author forever-king
 */
@Component
@Slf4j
public class RoleInterceptor implements HandlerInterceptor {


    private final JWTUtils jwtUtils;
    private final ObjectMapper objectMapper;
    private final GetUserIdViaTokenApi getUserIdViaTokenApi;
    private final UserMapper userMapper;

    public RoleInterceptor(JWTUtils jwtUtils, ObjectMapper objectMapper, GetUserIdViaTokenApi getUserIdViaTokenApi, UserMapper userMapper) {
        this.jwtUtils = jwtUtils;
        this.objectMapper = objectMapper;
        this.getUserIdViaTokenApi = getUserIdViaTokenApi;
        this.userMapper = userMapper;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        // here is the code that gets the RequireRole annotation
        RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);

        if (requireRole == null) {
            return true;
        }

        try {

            Long userId = getUserIdViaTokenApi.getUserId(request);

            String userRole = userMapper.getRoleByUserId(userId);

            if (userRole == null) {
                sendErrorResponse(response, HttpStatus.FORBIDDEN, "无法获取用户角色信息");
                return false;
            }

            UserRoleEnum[] requiredRoles = requireRole.value();
            boolean hasPermission = UserRoleEnum.hasPermission(userRole, requiredRoles);

            if (!hasPermission) {
                log.warn("用户权限不足，当前角色: {}, 需要角色: {}", 
                        UserRoleEnum.getByCode(Integer.parseInt(userRole)).getDescription(),
                        java.util.Arrays.toString(requiredRoles));
                sendErrorResponse(response, HttpStatus.FORBIDDEN, "权限不足，无法访问该接口");
                return false;
            }

            log.debug("权限验证通过，用户角色: {}", UserRoleEnum.getByCode(Integer.parseInt(userRole)).getDescription());
            return true;

        } catch (RuntimeException e) {
            log.error("Token解析失败: {}", e.getMessage());
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token无效或已过期");
            return false;
        }
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", message);
        result.put("code", status.value());

        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
