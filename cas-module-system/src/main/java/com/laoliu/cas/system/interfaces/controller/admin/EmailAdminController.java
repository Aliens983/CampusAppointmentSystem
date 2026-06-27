package com.laoliu.cas.system.interfaces.controller.admin;

import com.laoliu.cas.common.exception.code.EmailErrorCode;
import com.laoliu.cas.common.result.CommonResult;
import com.laoliu.cas.common.util.CodeGenerator;
import com.laoliu.cas.infra.application.service.EmailService;
import com.laoliu.cas.redis.util.RedisUtil;
import com.laoliu.cas.system.interfaces.dto.request.EmailRequest;
import com.laoliu.cas.system.interfaces.dto.response.EmailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员端邮件发送接口。
 *
 * @author forever-king
 */
@Tag(name = "邮件发送（管理）")
@RestController
@RequestMapping("/admin/email")
@RequiredArgsConstructor
public class EmailAdminController {

    private final EmailService emailService;
    private final RedisUtil redisUtil;

    @Operation(summary = "发送验证码邮件", description = "向指定邮箱发送验证码，60 秒内只能发送一次，Redis 缓存 5 分钟")
    @PostMapping
    public CommonResult<EmailResponse> sendEmail(@RequestBody EmailRequest request) {
        String email = request.getTo();
        if (email == null || email.isEmpty()) {
            return CommonResult.error(EmailErrorCode.EMAIL_NOT_PROVIDED);
        }

        String rateLimitKey = "rate_limit:email:" + email;
        String lastSendTime = redisUtil.getVerificationCode(rateLimitKey);
        if (lastSendTime != null) {
            return CommonResult.error(EmailErrorCode.EMAIL_SEND_TOO_FREQUENTLY);
        }

        String code = CodeGenerator.generateCode();
        String redisKey = "verification_code:" + email;
        redisUtil.setVerificationCode(redisKey, code, 300);
        redisUtil.setVerificationCode(rateLimitKey, "1", 60);

        String subject = "校园预约系统 - 邮箱验证码";
        String content = "您的验证码是：" + code + "，5 分钟内有效，请勿泄露给他人。";
        emailService.sendEmail(email, subject, content);

        return CommonResult.success(EmailResponse.builder().message("邮件发送成功").build());
    }
}
