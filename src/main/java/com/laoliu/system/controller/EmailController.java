package com.laoliu.system.controller;

import com.laoliu.system.common.CodeGenerator;
import com.laoliu.system.service.EmailSendService;
import com.laoliu.system.utils.RedisUtil;
import com.laoliu.system.vo.request.EmailRequest;
import com.laoliu.system.vo.response.EmailResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author 25516
 */
@RestController
@RequestMapping("email")
public class EmailController {

    private static final Logger log = LoggerFactory.getLogger(EmailController.class);

    private final EmailSendService emailSendService;
    
    private final RedisUtil redisUtil;

    public EmailController(EmailSendService emailSendService, RedisUtil redisUtil) {
        this.emailSendService = emailSendService;
        this.redisUtil = redisUtil;
    }
    
    @Value("${email.subject}")
    private String emailSubject;

    @Value("${email.content}")
    private String emailContent;

    // 验证码过期时间（秒），默认5分钟
    @Value("${email.code.expiration:300}")
    private int codeExpiration;
    
    // 邮件发送频率限制时间（秒），默认60秒内只能发送一次
    @Value("${email.send.frequency.limit:60}")
    private int frequencyLimit;


    @PostMapping
    public ResponseEntity<EmailResponse> sendEmail(@RequestBody EmailRequest request) {
        try {
            // 参数验证
            if (request == null) {
                return ResponseEntity.badRequest().body(new EmailResponse("请求参数不能为空", false));
            }
            String to = request.getTo();

            if (to == null || to.isEmpty()) {
                return ResponseEntity.badRequest().body(new EmailResponse("收件人邮箱不能为空", false));
            }

            // 检查发送频率限制
            String frequencyKey = "email_frequency_limit:" + to;
            String frequencyFlag = redisUtil.getVerificationCode(frequencyKey);
            
            if (frequencyFlag != null) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                        .body(new EmailResponse("发送过于频繁，请稍后再试", false));
            }

            String subject = emailSubject;
            
            // 生成验证码
            String verificationCode = CodeGenerator.generateCode();
            
            // 将验证码存储到Redis，设置过期时间
            redisUtil.setVerificationCode(to, verificationCode, codeExpiration);
            
            // 设置频率限制标记，防止在指定时间内重复发送
            redisUtil.setVerificationCode(frequencyKey, "sent", frequencyLimit);
            
            String content = emailContent+ " "+verificationCode+" ,打死都不要告诉别人!!!";
            log.info("邮件发送请求处理开始，收件人：{}，主题：{}，内容：{}", to, subject, content);

            // 发送邮件
            emailSendService.sendEmail(to, subject, content);
            log.info("邮件发送请求处理成功，收件人：{}，主题：{}，验证码已存入Redis并设置{}秒后过期，频率限制{}秒", 
                     to, subject, codeExpiration, frequencyLimit);
            return ResponseEntity.ok(new EmailResponse("邮件发送成功"));
        } catch (RuntimeException e) {
            log.error("邮件发送请求处理失败，错误信息：{}", e.getCause());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EmailResponse("邮件发送失败：" + e.getMessage(), false));
        } catch (Exception e) {
            log.error("邮件发送请求处理异常，错误信息：{}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EmailResponse("邮件发送失败：系统异常", false));
        }
    }
}
