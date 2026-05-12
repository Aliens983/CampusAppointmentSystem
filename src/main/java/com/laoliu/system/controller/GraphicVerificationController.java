package com.laoliu.system.controller;


import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.core.math.Calculator;
import com.laoliu.system.common.exception.enums.LoginErrorCode;
import com.laoliu.system.common.result.CommonResult;
import com.laoliu.system.utils.RedisUtil;
import com.laoliu.system.vo.request.VerifyCodeReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 * 图形验证码控制器
 * 
 * @author 25516
 */
@RestController
@RequestMapping("/graphic")
@Tag(name = "图形验证码")
public class GraphicVerificationController {

    private final RedisUtil redisUtil;

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.upload.prefix}")
    private String prefix;

    @Value("${file.upload.server-address}")
    private String serverAddress;

    public GraphicVerificationController(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @GetMapping("/get")
    @Operation(summary = "获取图形验证码")
    public CommonResult<Map<String, String>> getGraphicCaptcha() throws IOException {
        // 生成唯一的验证码标识符(UUID)
        String uuid = UUID.randomUUID().toString();
        String redisKey = "captcha:" + uuid;

        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(130, 38, 4, 4);
        // 自定义验证码内容为四则运算方式
        captcha.setGenerator(new MathGenerator());
        // 重新生成code
        captcha.createCode();

        String code = captcha.getCode();
        String expr = code.replace("=", "").trim();

        // 使用hutool的Calculator类进行计算
        double calcResult = Calculator.conversion(expr);
        String end = String.valueOf((int) calcResult);

        // 将验证码存储到Redis，使用UUID作为key
        redisUtil.setVerificationCode(redisKey, end, 300);

        // 保存验证码图片到本地文件
        String captchaDir = uploadPath + "captcha/";
        Path captchaPath = Paths.get(captchaDir);
        if (!Files.exists(captchaPath)) {
            Files.createDirectories(captchaPath);
        }

        String fileName = uuid + ".png";
        File imageFile = new File(captchaDir + fileName);
        captcha.write(imageFile);

        String imageUrl = serverAddress + prefix + "captcha/" + fileName;

        // 返回UUID和图片数据给前端
        Map<String, String> result = new HashMap<>();
        result.put("uuid", uuid);
        result.put("image", imageUrl);

        return CommonResult.success(result);
    }

    @GetMapping("/verify")
    @Operation(summary = "验证图形验证码")
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
            return CommonResult.success();
        }
        return CommonResult.error(LoginErrorCode.VERIFICATION_CODE_ERROR);
    }

    @PostMapping("/delete/{uuid}")
    @Operation(summary = "删除图形验证码(当用户点击刷新图像验证码是使用(前端直接先调用一次create再调用一次delete即可))")
    public CommonResult<String> deleteGraphicCaptcha(@PathVariable String uuid) {
        String redisKey = "captcha:" + uuid;
        redisUtil.removeVerificationCode(redisKey);
        return CommonResult.success();
    }

}
