package com.laoliu.cas.system.interfaces.controller;

import com.laoliu.cas.common.result.CommonResult;
import com.laoliu.cas.system.application.service.AuthService;
import com.laoliu.cas.system.application.service.vo.UserRegisterVO;
import com.laoliu.cas.system.interfaces.dto.request.UserRegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author forever-king
 */
@Tag(name = "用户注册")
@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final AuthService authService;

    @PostMapping("/verify-code")
    @Operation(summary = "验证邮箱验证码并登录")
    public CommonResult<Long> verifyEmailCode(@RequestBody UserRegisterRequest request) {
        UserRegisterVO vo = new UserRegisterVO();
        vo.setName(request.getName());
        vo.setGrade(request.getGrade());
        vo.setSex(request.getSex());
        vo.setAge(request.getAge());
        vo.setRole(request.getRole());
        vo.setEmail(request.getEmail());
        vo.setCode(request.getCode());
        vo.setPassword(request.getPassword());

        Long userId = authService.register(vo);
        return CommonResult.success(userId);
    }
}
