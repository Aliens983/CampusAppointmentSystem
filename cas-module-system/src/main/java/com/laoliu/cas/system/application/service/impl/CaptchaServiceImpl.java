package com.laoliu.cas.system.application.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.core.math.Calculator;
import com.laoliu.cas.redis.util.RedisUtil;
import com.laoliu.cas.system.application.service.CaptchaService;
import com.laoliu.cas.system.application.service.vo.CaptchaResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * @author forever-king
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {

    private final RedisUtil redisUtil;

    @Value("${file.upload.path:./uploads/}")
    private String uploadPath;

    @Value("${file.upload.prefix:/api/files/}")
    private String prefix;

    @Value("${file.upload.server-address:http://localhost:18080}")
    private String serverAddress;

    @Override
    public CaptchaResult generateCaptcha() {
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
        try {
            if (!Files.exists(captchaPath)) {
                Files.createDirectories(captchaPath);
            }
        } catch (IOException e) {
            log.error("创建验证码目录失败", e);
            throw new RuntimeException("创建验证码目录失败", e);
        }

        String fileName = uuid + ".png";
        File imageFile = new File(captchaDir + fileName);
        captcha.write(imageFile);

        String imageUrl = serverAddress + prefix + "captcha/" + fileName;

        return CaptchaResult.builder()
                .uuid(uuid)
                .imageUrl(imageUrl)
                .build();
    }
}
