package com.laoliu.system.controller;

import com.laoliu.system.common.exception.enums.UserErrorCode;
import com.laoliu.system.common.result.CommonResult;
import com.laoliu.system.converter.UserConverter;
import com.laoliu.system.entity.User;
import com.laoliu.system.mapper.UserMapper;
import com.laoliu.system.utils.JWTUtils;
import com.laoliu.system.utils.PasswordUtils;
import com.laoliu.system.utils.RedisUtil;
import com.laoliu.system.vo.request.UserRegisterRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
    public CommonResult<String> verifyEmailCode(@RequestBody UserRegisterRequest userRegisterRequest) {
        //TODO: Redis在此项目还有哪些可以使用的地方?这里还需要修改逻辑，如果注册的是管理员则需要填写管理员邀请码，如果注册的是不需要填写，还是说直接由超级管理员来创建管理员用户？
        try {
            String email = userRegisterRequest.getEmail();
            String code = userRegisterRequest.getCode();

            if (email == null || code == null) {
                return CommonResult.badRequest("邮箱和验证码不能为空");
            }

            Long ifUserId = userMapper.getUserIdByEmail(email);
            if (ifUserId != null) {
                return CommonResult.error(UserErrorCode.USER_ALREADY_EXISTS);
            }

            String storedCode = redisUtil.getVerificationCode(email);

            if (storedCode == null) {
                return CommonResult.unauthorized("验证码不存在或已过期");
            }

            if (!storedCode.equals(code)) {
                return CommonResult.unauthorized("验证码错误");
            }

            redisUtil.removeVerificationCode("verification_code:" + email);

            String password = userRegisterRequest.getPassword();
            String encode = passwordUtils.encode(password);
            userRegisterRequest.setPassword(encode);

            User user = userConverter.convertUserRequestToUser(userRegisterRequest);
            userMapper.insertSelective(user);

            Long userId = user.getId();
            String token = jwtUtils.generateToken(userId);

            return CommonResult.success(token);
        } catch (Exception e) {
            return CommonResult.internalServerError("注册验证失败: " + e.getLocalizedMessage());
        }
    }
}
