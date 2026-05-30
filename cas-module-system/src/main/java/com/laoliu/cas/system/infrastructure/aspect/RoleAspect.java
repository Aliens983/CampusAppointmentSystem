package com.laoliu.cas.system.infrastructure.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laoliu.cas.common.annotation.RequireRole;
import com.laoliu.cas.common.enums.UserRoleEnum;
import com.laoliu.cas.common.api.GetUserIdViaTokenApi;
import com.laoliu.cas.system.infrastructure.persistence.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class RoleAspect {

    private final ObjectMapper objectMapper;
    private final GetUserIdViaTokenApi getUserIdViaTokenApi;
    private final UserMapper userMapper;

    @Pointcut("@annotation(com.laoliu.cas.common.annotation.RequireRole)")
    public void requireRolePointcut() {
    }

    @Around("requireRolePointcut()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RequireRole requireRole = signature.getMethod().getAnnotation(RequireRole.class);

        if (requireRole == null) {
            return joinPoint.proceed();
        }

        try {
            Object[] args = joinPoint.getArgs();
            HttpServletRequest request = null;
            HttpServletResponse response = null;
            for (Object arg : args) {
                if (arg instanceof HttpServletRequest httpServletRequest) {
                    request = httpServletRequest;
                }
                if (arg instanceof HttpServletResponse httpServletResponse) {
                    response = httpServletResponse;
                }
            }

            if (request == null || response == null) {
                return joinPoint.proceed();
            }

            Long userId = getUserIdViaTokenApi.getUserId(request);
            if (userId == null) {
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "用户未登录");
                return null;
            }

            String userRole = userMapper.getRoleByUserId(userId);

            if (userRole == null) {
                sendErrorResponse(response, HttpStatus.FORBIDDEN, "无法获取用户角色信息");
                return null;
            }

            UserRoleEnum[] requiredRoles = requireRole.value();
            boolean hasPermission = hasPermission(userRole, requiredRoles);

            if (!hasPermission) {
                log.warn("用户权限不足，当前角色: {}, 需要角色: {}",
                        UserRoleEnum.getByCode(Integer.parseInt(userRole)).getDescription(),
                        Arrays.toString(requiredRoles));
                sendErrorResponse(response, HttpStatus.FORBIDDEN, "权限不足，无法访问该接口");
                return null;
            }

            log.debug("权限验证通过，用户角色: {}", UserRoleEnum.getByCode(Integer.parseInt(userRole)).getDescription());
            return joinPoint.proceed();

        } catch (RuntimeException e) {
            log.error("Token解析失败: {}", e.getMessage());
            Object[] args = joinPoint.getArgs();
            for (Object arg : args) {
                if (arg instanceof HttpServletResponse httpResponse) {
                    sendErrorResponse(httpResponse, HttpStatus.UNAUTHORIZED, "Token无效或已过期");
                    break;
                }
            }
            return null;
        }
    }

    private boolean hasPermission(String userRole, UserRoleEnum[] requiredRoles) {
        for (UserRoleEnum role : requiredRoles) {
            if (role.getCode() == Integer.parseInt(userRole)) {
                return true;
            }
        }
        return false;
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
