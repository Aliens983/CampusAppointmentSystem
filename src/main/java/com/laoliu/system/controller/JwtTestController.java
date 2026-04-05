package com.laoliu.system.controller;

import com.laoliu.system.common.result.CommonResult;
import com.laoliu.system.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
    public CommonResult<Map<String, Object>> parseToken(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        try {
            // 移除 "Bearer " 前缀
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Claims claims = jwtUtils.parseToken(token);
            Map<String, Object> result = new HashMap<>();
            result.put("userId", claims.getSubject());
            result.put("claims", claims);
            log.info("Token解析成功，用户ID：{}", claims.getSubject());
            return CommonResult.success(result);
        } catch (Exception e) {
            log.error("Token解析失败", e);
            Map<String, Object> result = new HashMap<>();
            result.put("message", e.getMessage());
            return CommonResult.error(401, "Token解析失败: " + e.getMessage());
        }
    }
}
