package com.laoliu.cas.common.service;

public interface EmailService {
    void sendEmail(String to, String subject, String content);
}
