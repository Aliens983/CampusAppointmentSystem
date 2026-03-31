package com.laoliu.system.controller;

import com.laoliu.system.converter.UserConverter;
import com.laoliu.system.entity.User;
import com.laoliu.system.mapper.UserMapper;
import com.laoliu.system.utils.JWTUtils;
import com.laoliu.system.utils.PasswordUtils;
import com.laoliu.system.utils.RedisUtil;
import com.laoliu.system.vo.request.UserRegisterRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author 25516
 */
@RestController
@RequestMapping("/register")
public class RegisterController {

    private final RedisUtil redisUtil;

    private final JWTUtils jwtUtils;

    private final UserMapper userMapper;

    private final UserConverter userConverter;

    private final PasswordUtils passwordUtils;

    public RegisterController(RedisUtil redisUtil, JWTUtils jwtUtils, UserMapper userMapper, UserConverter userConverter, PasswordUtils passwordUtils) {
        this.redisUtil = redisUtil;
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
        this.userConverter = userConverter;
        this.passwordUtils = passwordUtils;
    }



    /**
     * 验证邮箱验证码并登录
     */
    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyEmailCode(@RequestBody UserRegisterRequest userRegisterRequest) {
        //TODO: Redis在此项目还有哪些可以使用的地方?
        try {
            String email = userRegisterRequest.getEmail();
            String code = userRegisterRequest.getCode();


            if (email == null || code == null) {
                return ResponseEntity.badRequest().body("邮箱和验证码不能为空");
            }

            Long ifUserId = userMapper.getUserIdByEmail(email);
            if (ifUserId != null) {
                return ResponseEntity.badRequest().body("邮箱已存在账号,请直接登录");
            }

            // 从Redis获取存储的验证码
            String storedCode = redisUtil.getVerificationCode(email);

            if (storedCode == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("验证码不存在或已过期");
            }

            if (!storedCode.equals(code)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("验证码错误");
            }

            // 验证码正确，删除Redis中的验证码（防止重复使用）
            redisUtil.removeVerificationCode("verification_code:" + email);


            String password = userRegisterRequest.getPassword();
            String encode = passwordUtils.encode(password);
            userRegisterRequest.setPassword(encode);


            User user = userConverter.convertUserRequestToUser(userRegisterRequest);
            userMapper.insertSelective(user);

            // 生成JWT Token

            Long userId = user.getId();

            String token = jwtUtils.generateToken(userId);

            return ResponseEntity.ok(token);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("登录验证失败：" + e.getLocalizedMessage());
        }
    }

}
