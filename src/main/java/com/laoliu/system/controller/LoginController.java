package com.laoliu.system.controller;

import com.laoliu.system.utils.JWTUtils;
import com.laoliu.system.utils.RedisUtil;
import com.laoliu.system.vo.request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author 25516
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    private final RedisUtil redisUtil;

    private final JWTUtils jwtUtils;

    public LoginController(RedisUtil redisUtil, JWTUtils jwtUtils) {
        this.redisUtil = redisUtil;
        this.jwtUtils = jwtUtils;
    }

    /**
     * 验证邮箱验证码并登录
     */
    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyEmailCode(@RequestBody UserRequest userRequest) {
        try {
            String email = userRequest.getEmail();
            String code = userRequest.getCode();

            if (email == null || code == null) {
                return ResponseEntity.badRequest().body("邮箱和验证码不能为空");
            }

            // 从Redis获取存储的验证码
            String storedCode = redisUtil.getVerificationCode("verification_code:" + email);

            if (storedCode == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("验证码不存在或已过期");
            }

            if (!storedCode.equals(code)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("验证码错误");
            }

            // 验证码正确，删除Redis中的验证码（防止重复使用）
            redisUtil.removeVerificationCode("verification_code:" + email);

            // 生成JWT Token
            String token = jwtUtils.generateJwt();

            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("登录验证失败：" + e.getMessage());
        }
    }

}
