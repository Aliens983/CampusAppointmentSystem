package com.laoliu.system.controller;

import com.laoliu.system.common.CodeGenerator;
import com.laoliu.system.service.EmailSendService;
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

    public EmailController(EmailSendService emailSendService) {
        this.emailSendService = emailSendService;
    }
    @Value("${email.subject}")
    private String emailSubject;

    @Value("${email.content}")
    private String emailContent;


    @PostMapping
    public ResponseEntity<EmailResponse> sendEmail(@RequestBody EmailRequest request) {
        try {
            // 参数验证
            if (request == null) {
                return ResponseEntity.badRequest().body(new EmailResponse("请求参数不能为空", false));
            }
            String to = request.getTo();
            String subject = emailSubject;
            String content = emailContent+ " "+CodeGenerator.generateCode()+" ,打死都不要告诉别人!!!";
            log.info("邮件发送请求处理开始，收件人：{}，主题：{}，内容：{}", to, subject, content);

            if (to == null || to.isEmpty()) {
                return ResponseEntity.badRequest().body(new EmailResponse("收件人邮箱不能为空", false));
            }

            // 发送邮件
            emailSendService.sendEmail(to, subject, content);
            log.info("邮件发送请求处理成功，收件人：{}，主题：{}", to, subject);
            return ResponseEntity.ok(new EmailResponse("邮件发送成功"));
        } catch (RuntimeException e) {
            log.error("邮件发送请求处理失败，错误信息：{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EmailResponse("邮件发送失败：" + e.getMessage(), false));
        } catch (Exception e) {
            log.error("邮件发送请求处理异常，错误信息：{}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EmailResponse("邮件发送失败：系统异常", false));
        }
    }
}
