package com.laoliu.cas.system.interfaces.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.core.math.Calculator;
import com.laoliu.cas.common.exception.code.LoginErrorCode;
import com.laoliu.cas.common.result.CommonResult;
import com.laoliu.cas.redis.util.RedisUtil;
import com.laoliu.cas.system.interfaces.dto.request.VerifyCodeReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author forever-king
 */
@RestController
@RequestMapping("/graphic")
@Tag(name = "图形验证码")
@RequiredArgsConstructor
public class GraphicVerificationController {

    private final RedisUtil redisUtil;

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.upload.prefix}")
    private String prefix;

    @Value("${file.upload.server-address}")
    private String serverAddress;

    @Operation(summary = "获取图形验证码", description = "获取数学计算类型的图形验证码，返回uuid和验证码图片URL，验证码5分钟内有效")
    @GetMapping("/get")
    public CommonResult<Map<String, String>> getGraphicCaptcha() throws IOException {
        String uuid = UUID.randomUUID().toString();
        String redisKey = "captcha:" + uuid;

        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(130, 38, 4, 4);
        captcha.setGenerator(new MathGenerator());
        captcha.createCode();

        String code = captcha.getCode();
        String expr = code.replace("=", "").trim();
        double calcResult = Calculator.conversion(expr);
        String end = String.valueOf((int) calcResult);

        redisUtil.setVerificationCode(redisKey, end, 300);

        String captchaDir = uploadPath + "captcha/";
        Path captchaPath = Paths.get(captchaDir);
        if (!Files.exists(captchaPath)) {
            Files.createDirectories(captchaPath);
        }

        String fileName = uuid + ".png";
        File imageFile = new File(captchaDir + fileName);
        captcha.write(imageFile);

        String imageUrl = serverAddress + prefix + "captcha/" + fileName;

        Map<String, String> result = new HashMap<>();
        result.put("uuid", uuid);
        result.put("image", imageUrl);

        return CommonResult.success(result);
    }

    @Operation(summary = "验证图形验证码", description = "提交UUID和验证码进行验证，验证成功后验证码自动失效")
    @GetMapping("/verify")
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
