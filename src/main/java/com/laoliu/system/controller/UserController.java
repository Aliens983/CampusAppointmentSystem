package com.laoliu.system.controller;

import com.laoliu.system.annotation.RequireRole;
import com.laoliu.system.converter.UserConverter;
import com.laoliu.system.entity.User;
import com.laoliu.system.enums.UserRoleEnum;
import com.laoliu.system.mapper.UserMapper;
import com.laoliu.system.utils.JWTUtils;
import com.laoliu.system.vo.response.UserResponse;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 25516
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    private final JWTUtils jwtUtils;
    private final UserMapper userMapper;
    private final UserConverter userConverter;

    public UserController(JWTUtils jwtUtils, UserMapper userMapper, UserConverter userConverter) {
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
        this.userConverter = userConverter;
    }

    @GetMapping
    @RequireRole(UserRoleEnum.USER)
    public ResponseEntity<Map<String, Object>> getUserByParseToken(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String token = request.getHeader("Authorization");
        try {
            // 移除 "Bearer " 前缀
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Claims claims = jwtUtils.parseToken(token);
            result.put("success", true);
            // 获取用户ID
            String userId = claims.getSubject();
            result.put("userId", userId);
            result.put("claims", claims);
            log.info("Token解析成功，用户ID：{}", userId);
            User user;
            user = userMapper.selectByPrimaryKey(Long.valueOf(userId));
            log.info("User :{}",user);
            UserResponse userResponse = userConverter.convertUserToUserResponse(user);
            result.put("user", userResponse);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/all_users")
    @RequireRole(UserRoleEnum.ADMIN)
    public ResponseEntity<Map<String, Object>> getAllUsers(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("success", true);
            result.put("users", userMapper.getAllUsers());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
