package com.laoliu.cas.thirdparty.aliyun.service;

public interface SmsService {
    void sendSms(String phoneNumber, String templateCode, String templateParam);
}
