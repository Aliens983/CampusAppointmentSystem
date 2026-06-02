package com.laoliu.cas.infra.service;

/**
 * @author forever-king
 */
public interface EmailService {
    void sendEmail(String to, String subject, String content);
}
