package com.laoliu.cas.system.application.service;

/**
 * @author forever-king
 */
public interface EmailVerificationService {

    void sendVerificationCode(String email);
}
