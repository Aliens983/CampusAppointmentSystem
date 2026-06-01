package com.laoliu.cas.common.service;

/**
 * @author forever-king
 */
public interface EmailService {
    void sendEmail(String to, String subject, String content);
}
