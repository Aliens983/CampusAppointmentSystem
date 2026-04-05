package com.laoliu.system.controller;

import com.laoliu.system.common.exception.enums.LoginErrorCode;
import com.laoliu.system.common.result.CommonResult;
import com.laoliu.system.mapper.UserMapper;
import com.laoliu.system.utils.JWTUtils;
import com.laoliu.system.utils.PasswordUtils;
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
    // TODO: 登录的时候可以新增一个忘记密码的功能，提供原始邮箱并且输入验证码重置登入账号返回token

    private final UserMapper userMapper;

    private final JWTUtils jwtUtils;

    private final PasswordUtils passwordUtils;

    public LoginController(UserMapper userMapper, JWTUtils jwtUtils, PasswordUtils passwordUtils) {
        this.userMapper = userMapper;
        this.jwtUtils = jwtUtils;
        this.passwordUtils = passwordUtils;
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
}
