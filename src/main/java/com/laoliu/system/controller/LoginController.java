package com.laoliu.system.controller;

import com.laoliu.system.common.exception.enums.LoginErrorCode;
import com.laoliu.system.common.result.CommonResult;
import com.laoliu.system.mapper.UserMapper;
import com.laoliu.system.utils.JWTUtils;
import com.laoliu.system.utils.PasswordUtils;
import com.laoliu.system.utils.RedisUtil;
import com.laoliu.system.vo.request.ResetPasswordRequest;
import com.laoliu.system.vo.request.UserLoginRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author forever-king
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    private final UserMapper userMapper;
    private final JWTUtils jwtUtils;
    private final PasswordUtils passwordUtils;
    private final RedisUtil redisUtil;

    public LoginController(UserMapper userMapper, JWTUtils jwtUtils, PasswordUtils passwordUtils, RedisUtil redisUtil) {
        this.userMapper = userMapper;
        this.jwtUtils = jwtUtils;
        this.passwordUtils = passwordUtils;
        this.redisUtil = redisUtil;
    }

    @PostMapping
    public CommonResult<String> login(@RequestBody UserLoginRequest userLoginRequest) {
        String email = userLoginRequest.getEmail();
        String password = userLoginRequest.getPassword();
        if (email == null || password == null) {
            return CommonResult.badRequest("邮箱和密码都不能为空");
        }

        String encodePassword = userMapper.getEncodePasswordByEmail(email);
        if (encodePassword == null) {
            return CommonResult.error(LoginErrorCode.USER_NOT_EXIST);
        }
        if (passwordUtils.matches(password, encodePassword)) {
            Long userId = userMapper.getUserIdByEmail(email);
            return CommonResult.success(jwtUtils.generateToken(userId));
        }
        return CommonResult.error(LoginErrorCode.PASSWORD_ERROR);
    }

    @PostMapping("/reset")
    public CommonResult<String> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        String email = resetPasswordRequest.getEmail();
        String code = resetPasswordRequest.getCode();
        String password = resetPasswordRequest.getPassword();

        if (email == null || email.isEmpty()) {
            return CommonResult.badRequest("邮箱不能为空");
        }
        if (code == null || code.isEmpty()) {
            return CommonResult.badRequest("验证码不能为空");
        }
        if (password == null || password.isEmpty()) {
            return CommonResult.badRequest("密码不能为空");
        }

        String storedCode = redisUtil.getVerificationCode(email);
        if (storedCode == null) {
            return CommonResult.error(LoginErrorCode.VERIFICATION_CODE_EXPIRED);
        }
        if (!storedCode.equals(code)) {
            return CommonResult.error(LoginErrorCode.VERIFICATION_CODE_ERROR);
        }

        Long userId = userMapper.getUserIdByEmail(email);

        if (userId == null) {
            return CommonResult.error(LoginErrorCode.USER_NOT_EXIST_BY_EMAIL);
        }

        String encodedPassword = passwordUtils.encode(password);
        userMapper.updatePasswordByEmail(email, encodedPassword);

        redisUtil.removeVerificationCode(email);

        return CommonResult.success(jwtUtils.generateToken(userId));
    }
}
