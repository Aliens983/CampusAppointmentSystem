package com.laoliu.system.common.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CodeGeneratorTest {

    @Test
    @DisplayName("生成的验证码应为6位")
    void generateCode_shouldReturn6Digits() {
        String code = CodeGenerator.generateCode();
        assertNotNull(code);
        assertEquals(6, code.length());
    }

    @RepeatedTest(100)
    @DisplayName("生成的验证码应只包含数字")
    void generateCode_shouldOnlyContainDigits() {
        String code = CodeGenerator.generateCode();
        assertTrue(code.matches("\\d{6}"));
    }

    @Test
    @DisplayName("多次生成的验证码应不同")
    void generateCode_shouldBeRandom() {
        String code1 = CodeGenerator.generateCode();
        String code2 = CodeGenerator.generateCode();
        assertNotEquals(code1, code2);
    }

    @Test
    @DisplayName("生成的验证码应在000000-999999范围内")
    void generateCode_shouldBeInValidRange() {
        String code = CodeGenerator.generateCode();
        int intCode = Integer.parseInt(code);
        assertTrue(intCode >= 0 && intCode <= 999999);
    }
}
