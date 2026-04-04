package com.laoliu.system.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laoliu.system.annotation.RequireRole;
import com.laoliu.system.api.GetUserIdViaTokenApi;
import com.laoliu.system.enums.UserRoleEnum;
import com.laoliu.system.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @author forever-king
 */
@Aspect
@Component
@Slf4j
public class RoleAspect {

    private final ObjectMapper objectMapper;
    private final GetUserIdViaTokenApi getUserIdViaTokenApi;
    private final UserMapper userMapper;

    public RoleAspect(ObjectMapper objectMapper, GetUserIdViaTokenApi getUserIdViaTokenApi, UserMapper userMapper) {
        this.objectMapper = objectMapper;
        this.getUserIdViaTokenApi = getUserIdViaTokenApi;
        this.userMapper = userMapper;
    }

    @Pointcut("@annotation(com.laoliu.system.annotation.RequireRole)")
    public void requireRolePointcut() {
    }

    @Around("requireRolePointcut()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取注解信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RequireRole requireRole = signature.getMethod().getAnnotation(RequireRole.class);

        // 如果没有注解，则直接执行方法
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

            String userRole = userMapper.getRoleByUserId(userId);

            if (userRole == null) {
                sendErrorResponse(response, HttpStatus.FORBIDDEN, "无法获取用户角色信息");
                return null;
            }

            UserRoleEnum[] requiredRoles = requireRole.value();
            boolean hasPermission = UserRoleEnum.hasPermission(userRole, requiredRoles);

            if (!hasPermission) {
                log.warn("用户权限不足，当前角色: {}, 需要角色: {}", 
                        UserRoleEnum.getByCode(Integer.parseInt(userRole)).getDescription(),
                        java.util.Arrays.toString(requiredRoles));
                sendErrorResponse(response, HttpStatus.FORBIDDEN, "权限不足，无法访问该接口");
                return null;
            }

            log.debug("权限验证通过，用户角色: {}", UserRoleEnum.getByCode(Integer.parseInt(userRole)).getDescription());
            return joinPoint.proceed();

        } catch (RuntimeException e) {
            log.error("Token解析失败: {}", e.getMessage());
            Object[] args = joinPoint.getArgs();
            for (Object arg : args) {
                if (arg instanceof HttpServletResponse response) {
                    sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token无效或已过期");
                    break;
                }
            }
            return null;
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
