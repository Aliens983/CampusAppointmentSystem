package com.laoliu.cas.thirdparty.service;

/**
 * @author forever-king
 */
public interface SmsService {
    void sendSms(String phoneNumber, String templateCode, String templateParam);
}
