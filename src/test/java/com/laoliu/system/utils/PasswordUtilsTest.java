package com.laoliu.system.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PasswordUtilsTest {

    private final PasswordUtils passwordUtils = new PasswordUtils();

    @Test
    @DisplayName("加密密码不应与原文相同")
    void encode_shouldNotReturnRawPassword() {
        String rawPassword = "test123456";
        String encoded = passwordUtils.encode(rawPassword);
        assertNotNull(encoded);
        assertNotEquals(rawPassword, encoded);
    }

    @Test
    @DisplayName("加密后应能匹配原始密码")
    void matches_shouldReturnTrueForCorrectPassword() {
        String rawPassword = "test123456";
        String encoded = passwordUtils.encode(rawPassword);
        assertTrue(passwordUtils.matches(rawPassword, encoded));
    }

    @Test
    @DisplayName("错误密码应不匹配")
    void matches_shouldReturnFalseForWrongPassword() {
        String rawPassword = "test123456";
        String encoded = passwordUtils.encode(rawPassword);
        assertFalse(passwordUtils.matches("wrong_password", encoded));
    }

    @Test
    @DisplayName("加密结果每次应不同（随机盐值）")
    void encode_shouldProduceDifferentHashes() {
        String rawPassword = "test123456";
        String hash1 = passwordUtils.encode(rawPassword);
        String hash2 = passwordUtils.encode(rawPassword);
        assertNotEquals(hash1, hash2);
        assertTrue(passwordUtils.matches(rawPassword, hash1));
        assertTrue(passwordUtils.matches(rawPassword, hash2));
    }

    @Test
    @DisplayName("空密码应能加密和匹配")
    void encode_shouldHandleEmptyPassword() {
        String encoded = passwordUtils.encode("");
        assertNotNull(encoded);
        assertTrue(passwordUtils.matches("", encoded));
    }

    @Test
    @DisplayName("null密码应抛出异常")
    void matches_shouldThrowExceptionForNullRawPassword() {
        assertThrows(IllegalArgumentException.class,
                () -> passwordUtils.matches(null, "encoded"));
    }
}
