package com.laoliu.system.common.enums;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * @author 25516
 */
@Component
public class CodeGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();

    // 生成6位数的验证码
    public static String generateCode() {
        return String.format("%06d", RANDOM.nextInt(1000000));
    }

}
