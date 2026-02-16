package com.laoliu.system.controller;

import com.laoliu.system.entity.User;
import com.laoliu.system.mapper.UserMapper;
import com.laoliu.system.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
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

    public UserController(JWTUtils jwtUtils, UserMapper userMapper) {
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
    }

    @GetMapping
    public User getUserByParseToken(HttpServletRequest request) {
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
            return user;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return null;
        }

    }
}
