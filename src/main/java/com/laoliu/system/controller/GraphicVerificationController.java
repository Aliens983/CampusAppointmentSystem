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
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

        // 将图片转换为Base64字符串
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        captcha.write(outputStream);
        outputStream.close();
        byte[] imageBytes = outputStream.toByteArray();
        String base64Image = "data:image/jpeg;base64," + java.util.Base64.getEncoder().encodeToString(imageBytes);

        String code = captcha.getCode();
        String expr = code.replace("=", "").trim();

        // 使用hutool的Calculator类进行计算
        double calcResult = Calculator.conversion(expr);
        String end = String.valueOf((int) calcResult);

        // 将验证码存储到Redis，使用UUID作为key
        redisUtil.setVerificationCode(redisKey, end, 300);

        // 返回UUID和图片数据给前端
        Map<String, String> result = new HashMap<>();
        result.put("uuid", uuid);
        result.put("image", base64Image);
        
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
