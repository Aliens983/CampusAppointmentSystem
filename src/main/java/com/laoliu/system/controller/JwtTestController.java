package com.laoliu.system.controller;

import com.laoliu.system.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 25516
 */
@Slf4j
@RestController
@RequestMapping("/api/jwt")
public class JwtTestController {

    private final JWTUtils jwtUtils;

    public JwtTestController(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/parse")
    public Map<String, Object> parseToken(@RequestHeader("Authorization") String token) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 移除 "Bearer " 前缀
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Claims claims = jwtUtils.parseToken(token);
            result.put("success", true);
            // 获取用户ID
            result.put("userId", claims.getSubject());
            result.put("claims", claims);
            log.info("Token解析成功，用户ID：{}", claims.getSubject());
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return result;
        }
    }
}
