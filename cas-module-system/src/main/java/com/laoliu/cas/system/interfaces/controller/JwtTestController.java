package com.laoliu.cas.system.interfaces.controller;

import com.laoliu.cas.common.result.CommonResult;
import com.laoliu.cas.security.util.JWTUtils;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/jwt")
@RequiredArgsConstructor
@Tag(name = "JWT测试接口")
public class JwtTestController {

    private final JWTUtils jwtUtils;

    @PostMapping("/parse")
    @Operation(summary = "解析JWT")
    public CommonResult<Map<String, Object>> parseToken(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        try {
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
