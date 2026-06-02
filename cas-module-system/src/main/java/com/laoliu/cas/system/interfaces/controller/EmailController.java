package com.laoliu.cas.system.interfaces.controller;

import com.laoliu.cas.common.exception.code.EmailErrorCode;
import com.laoliu.cas.common.util.CodeGenerator;
import com.laoliu.cas.redis.util.RedisUtil;
import com.laoliu.cas.infra.service.EmailService;
import com.laoliu.cas.system.interfaces.dto.request.EmailRequest;
import com.laoliu.cas.system.interfaces.dto.response.EmailResponse;
import com.laoliu.cas.common.result.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @author forever-king
 */
@Slf4j
@Tag(name = "邮件发送")
@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final RedisUtil redisUtil;

    @Value("${email.subject:验证码}")
    private String emailSubject;

    @Value("${email.content:您的验证码是：}")
    private String emailContent;

    @Value("${email.code.expiration:300}")
    private int codeExpiration;

    @Value("${email.send.frequency.limit:60}")
    private int frequencyLimit;

    @Operation(summary = "发送验证码邮件", description = "向指定邮箱发送验证码，验证码5分钟内有效，同一邮箱60秒内只能发送一次")
    @PostMapping
    public CommonResult<EmailResponse> sendEmail(@RequestBody EmailRequest request) {
        try {
            if (request == null) {
                return CommonResult.badRequest("请求参数不能为空");
            }
            String to = request.getTo();

            if (to == null || to.isEmpty()) {
                return CommonResult.badRequest("收件人邮箱不能为空");
            }

            String frequencyKey = "email_frequency_limit:" + to;
            String frequencyFlag = redisUtil.getVerificationCode(frequencyKey);

            if (frequencyFlag != null) {
                return CommonResult.error(EmailErrorCode.EMAIL_SEND_TOO_FREQUENTLY);
            }

            String subject = emailSubject;
            String verificationCode = CodeGenerator.generateCode();

            redisUtil.setVerificationCode(to, verificationCode, codeExpiration);
            redisUtil.setVerificationCode(frequencyKey, "sent", frequencyLimit);

            String content = emailContent + " " + verificationCode + " ,打死都不要告诉别人!!!";
            log.info("邮件发送请求处理开始，收件人：{}，主题：{}，内容：{}", to, subject, content);

            emailService.sendEmail(to, subject, content);
            log.info("邮件发送请求处理成功，收件人：{}，主题：{}，验证码已存入Redis并设置{}秒后过期，频率限制{}秒",
                    to, subject, codeExpiration, frequencyLimit);

            EmailResponse emailResponse = new EmailResponse("邮件发送成功");
            return CommonResult.success(emailResponse);
        } catch (RuntimeException e) {
            log.error("邮件发送请求处理失败，错误信息：{}", e.getCause());
            return CommonResult.internalServerError("邮件发送失败: " + e.getMessage());
        } catch (Exception e) {
            log.error("邮件发送请求处理异常，错误信息：{}", e.getMessage(), e);
            return CommonResult.internalServerError("邮件发送失败: 系统异常");
        }
    }
}
