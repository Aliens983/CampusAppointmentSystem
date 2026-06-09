package com.laoliu.cas.system.interfaces.controller.app;

import com.laoliu.cas.common.result.CommonResult;
import com.laoliu.cas.system.application.service.AuthService;
import com.laoliu.cas.system.interfaces.dto.request.ResetPasswordRequest;
import com.laoliu.cas.system.interfaces.dto.request.UserLoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户端登录相关接口。
 *
 * @author forever-king
 */
@Tag(name = "登录接口（用户）")
@RestController
@RequestMapping("/app/login")
@RequiredArgsConstructor
public class LoginAppController {

    private final AuthService authService;

    @Operation(summary = "用户登录", description = "用户通过邮箱和密码登录，返回JWT令牌")
    @PostMapping
    public CommonResult<String> login(@RequestBody UserLoginRequest userLoginRequest) {
        String token = authService.login(userLoginRequest.getEmail(), userLoginRequest.getPassword());
        return CommonResult.success(token);
    }

    @Operation(summary = "重置密码", description = "通过邮箱验证码重置密码，验证成功后返回新的JWT令牌")
    @PostMapping("/reset")
    public CommonResult<String> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        String token = authService.resetPassword(
                resetPasswordRequest.getEmail(),
                resetPasswordRequest.getCode(),
                resetPasswordRequest.getPassword()
        );
        return CommonResult.success(token);
    }
}
