package com.laoliu.cas.system.interfaces.controller.app;

import com.laoliu.cas.common.exception.code.LoginErrorCode;
import com.laoliu.cas.common.result.CommonResult;
import com.laoliu.cas.redis.util.RedisUtil;
import com.laoliu.cas.system.application.service.CaptchaService;
import com.laoliu.cas.system.application.service.vo.CaptchaResult;
import com.laoliu.cas.system.interfaces.dto.request.VerifyCodeReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户端图形验证码接口。
 *
 * @author forever-king
 */
@RestController
@RequestMapping("/app/graphic")
@Tag(name = "图形验证码（用户）")
@RequiredArgsConstructor
public class GraphicVerificationAppController {

    private final RedisUtil redisUtil;
    private final CaptchaService captchaService;

    @Operation(summary = "获取图形验证码", description = "获取数学计算类型的图形验证码，返回uuid和验证码图片URL，验证码5分钟内有效")
    @GetMapping("/get")
    public CommonResult<Map<String, String>> getGraphicCaptcha() {
        CaptchaResult captchaResult = captchaService.generateCaptcha();

        Map<String, String> result = new HashMap<>();
        result.put("uuid", captchaResult.getUuid());
        result.put("image", captchaResult.getImageUrl());

        return CommonResult.success(result);
    }

    @Operation(summary = "验证图形验证码", description = "提交UUID和验证码进行验证，验证成功后验证码自动失效")
    @PostMapping("/verify")
    public CommonResult<String> verifyGraphicCaptcha(@RequestBody VerifyCodeReqVO reqVO) {
        String uuid = reqVO.getUuid();
        String code = reqVO.getCode();
        String redisKey = "captcha:" + uuid;
        String redisCode = redisUtil.getVerificationCode(redisKey);
        if (redisCode == null) {
            return CommonResult.error(LoginErrorCode.VERIFICATION_CODE_EXPIRED);
        }
        if (redisCode.equals(code)) {
            redisUtil.removeVerificationCode(redisKey);
            return CommonResult.success("验证成功");
        }
        return CommonResult.error(LoginErrorCode.VERIFICATION_CODE_ERROR);
    }

    @PostMapping("/delete/{uuid}")
    @Operation(summary = "删除图形验证码")
    public CommonResult<String> deleteGraphicCaptcha(@PathVariable String uuid) {
        String redisKey = "captcha:" + uuid;
        redisUtil.removeVerificationCode(redisKey);
        return CommonResult.success("删除成功");
    }
}
