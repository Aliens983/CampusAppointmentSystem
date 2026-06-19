package com.laoliu.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laoliu.system.service.EmailSendService;
import com.laoliu.system.utils.RedisUtil;
import com.laoliu.system.vo.request.EmailRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EmailControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private EmailSendService emailSendService;

    @Mock
    private RedisUtil redisUtil;

    @InjectMocks
    private EmailController emailController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(emailController).build();
        ReflectionTestUtils.setField(emailController, "emailSubject", "Test Subject");
        ReflectionTestUtils.setField(emailController, "emailContent", "Your verification code is:");
        ReflectionTestUtils.setField(emailController, "codeExpiration", 300);
        ReflectionTestUtils.setField(emailController, "frequencyLimit", 60);
    }

    @Test
    @DisplayName("发送邮件成功应返回200")
    void sendEmail_success_shouldReturn200() throws Exception {
        EmailRequest request = new EmailRequest();
        request.setTo("test@test.com");

        when(redisUtil.getVerificationCode("email_frequency_limit:test@test.com")).thenReturn(null);

        mockMvc.perform(post("/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(emailSendService).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("发送邮件时收件人为空应返回400")
    void sendEmail_emptyTo_shouldReturn400() throws Exception {
        EmailRequest request = new EmailRequest();
        request.setTo("");

        mockMvc.perform(post("/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    @DisplayName("发送邮件频率过快应返回429")
    void sendEmail_tooFrequent_shouldReturn429() throws Exception {
        EmailRequest request = new EmailRequest();
        request.setTo("test@test.com");

        when(redisUtil.getVerificationCode("email_frequency_limit:test@test.com")).thenReturn("sent");

        mockMvc.perform(post("/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(429));
    }
}
