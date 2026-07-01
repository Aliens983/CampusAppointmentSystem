package com.laoliu.cas.system.interfaces.controller.app;

import com.laoliu.cas.common.result.CommonResult;
import com.laoliu.cas.system.application.service.CaptchaService;
import com.laoliu.cas.system.application.service.vo.CaptchaResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 图形验证码接口（扁平路径，供前端直接调用）。
 *
 * @author forever-king
 */
@Tag(name = "图形验证码（用户）")
@RestController
@RequestMapping("/graphic")
@RequiredArgsConstructor
public class GraphicController {

    private final CaptchaService captchaService;

    @Operation(summary = "获取图形验证码", description = "返回uuid和验证码图片URL，验证码5分钟内有效")
    @GetMapping("/get")
    public CommonResult<Map<String, String>> getGraphicCaptcha() {
        CaptchaResult captchaResult = captchaService.generateCaptcha();

        Map<String, String> result = new HashMap<>();
        result.put("uuid", captchaResult.getUuid());
        result.put("image", captchaResult.getImageUrl());

        return CommonResult.success(result);
    }
}
