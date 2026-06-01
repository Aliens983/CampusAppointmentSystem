package com.laoliu.cas.system.interfaces.controller;

import com.laoliu.cas.common.result.CommonResult;
import com.laoliu.cas.security.util.JWTUtils;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author forever-king
 */
@Slf4j
@RestController
@RequestMapping("/api/jwt")
@RequiredArgsConstructor
@Tag(name = "JWT测试接口")
public class JwtTestController {

    private final JWTUtils jwtUtils;

    @Operation(summary = "解析JWT", description = "解析JWT令牌并提取用户ID和声明信息，支持Bearer前缀或不带前缀的令牌")
    @PostMapping("/parse")
    public CommonResult<Map<String, Object>> parseToken(@Parameter(description = "JWT令牌", required = true) @RequestBody Map<String, String> request) {
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
