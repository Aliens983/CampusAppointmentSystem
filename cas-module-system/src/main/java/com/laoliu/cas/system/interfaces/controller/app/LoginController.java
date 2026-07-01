package com.laoliu.cas.system.interfaces.controller.app;

import com.laoliu.cas.common.result.CommonResult;
import com.laoliu.cas.system.application.service.AuthService;
import com.laoliu.cas.system.interfaces.dto.request.UserLoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录接口（扁平路径，供前端直接调用）。
 *
 * @author forever-king
 */
@Tag(name = "登录接口（用户）")
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;

    @Operation(summary = "用户登录", description = "用户通过邮箱和密码登录，返回JWT令牌")
    @PostMapping
    public CommonResult<String> login(@RequestBody UserLoginRequest userLoginRequest) {
        String token = authService.login(userLoginRequest.getEmail(), userLoginRequest.getPassword());
        return CommonResult.success(token);
    }
}
