package com.laoliu.system.service;

/**
 * @author 25516
 */
public interface EmailSendService {
    void sendEmail(String to, String subject, String content);
}
