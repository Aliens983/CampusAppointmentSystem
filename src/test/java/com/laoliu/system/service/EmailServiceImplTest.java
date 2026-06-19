package com.laoliu.system.service;

import com.laoliu.system.service.impl.EmailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private Environment environment;

    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        emailService = new EmailServiceImpl(javaMailSender, environment);
    }

    @Test
    @DisplayName("sendEmail 成功应调用JavaMailSender")
    void sendEmail_success_shouldCallMailSender() {
        when(environment.getProperty("spring.mail.username")).thenReturn("test@test.com");

        emailService.sendEmail("to@test.com", "Subject", "Content");

        verify(javaMailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("sendEmail 发送失败应抛出异常")
    void sendEmail_failure_shouldThrow() {
        when(environment.getProperty("spring.mail.username")).thenReturn("test@test.com");
        doThrow(new RuntimeException("SMTP error")).when(javaMailSender).send(any(SimpleMailMessage.class));

        assertThrows(RuntimeException.class,
                () -> emailService.sendEmail("to@test.com", "Subject", "Content"));
    }
}
