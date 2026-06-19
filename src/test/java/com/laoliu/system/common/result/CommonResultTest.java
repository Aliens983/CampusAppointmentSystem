package com.laoliu.system.common.result;

import com.laoliu.system.common.exception.ErrorCode;
import com.laoliu.system.common.exception.enums.CommonErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonResultTest {

    @Test
    @DisplayName("success() 应返回 code=200")
    void success_noData_shouldReturn200() {
        CommonResult<Void> result = CommonResult.success();
        assertEquals(200, result.getCode());
        assertEquals("操作成功", result.getMessage());
        assertNull(result.getData());
        assertTrue(result.isSuccess());
    }

    @Test
    @DisplayName("success(data) 应返回 code=200 并携带数据")
    void success_withData_shouldReturn200WithData() {
        CommonResult<String> result = CommonResult.success("hello");
        assertEquals(200, result.getCode());
        assertEquals("操作成功", result.getMessage());
        assertEquals("hello", result.getData());
        assertTrue(result.isSuccess());
    }

    @Test
    @DisplayName("success(message, data) 应返回自定义消息")
    void success_withMessageAndData_shouldReturnCustomMessage() {
        CommonResult<String> result = CommonResult.success("自定义成功", "data");
        assertEquals(200, result.getCode());
        assertEquals("自定义成功", result.getMessage());
        assertEquals("data", result.getData());
    }

    @Test
    @DisplayName("error(code, message) 应返回错误")
    void error_withCodeAndMessage_shouldReturnError() {
        CommonResult<Void> result = CommonResult.error(400, "错误信息");
        assertEquals(400, result.getCode());
        assertEquals("错误信息", result.getMessage());
        assertNull(result.getData());
        assertTrue(result.isError());
    }

    @Test
    @DisplayName("error(ErrorCode) 应使用ErrorCode中的code和message")
    void error_withErrorCode_shouldUseErrorCode() {
        CommonResult<Void> result = CommonResult.error(CommonErrorCode.BAD_REQUEST);
        assertEquals(400, result.getCode());
        assertEquals("请求参数错误", result.getMessage());
    }

    @Test
    @DisplayName("badRequest 应返回400")
    void badRequest_shouldReturn400() {
        CommonResult<Void> result = CommonResult.badRequest("参数错误");
        assertEquals(400, result.getCode());
        assertEquals("参数错误", result.getMessage());
    }

    @Test
    @DisplayName("unauthorized 应返回401")
    void unauthorized_shouldReturn401() {
        CommonResult<Void> result = CommonResult.unauthorized("未授权");
        assertEquals(401, result.getCode());
        assertEquals("未授权", result.getMessage());
    }

    @Test
    @DisplayName("forbidden 应返回403")
    void forbidden_shouldReturn403() {
        CommonResult<Void> result = CommonResult.forbidden("禁止访问");
        assertEquals(403, result.getCode());
        assertEquals("禁止访问", result.getMessage());
    }

    @Test
    @DisplayName("notFound 应返回404")
    void notFound_shouldReturn404() {
        CommonResult<Void> result = CommonResult.notFound("资源不存在");
        assertEquals(404, result.getCode());
        assertEquals("资源不存在", result.getMessage());
    }

    @Test
    @DisplayName("internalServerError 应返回500")
    void internalServerError_shouldReturn500() {
        CommonResult<Void> result = CommonResult.internalServerError("服务器错误");
        assertEquals(500, result.getCode());
        assertEquals("服务器错误", result.getMessage());
    }

    @Test
    @DisplayName("getCheckedData 在成功时应返回data")
    void getCheckedData_shouldReturnDataOnSuccess() {
        CommonResult<String> result = CommonResult.success("data");
        assertEquals("data", result.getCheckedData());
    }

    @Test
    @DisplayName("getCheckedData 在失败时应抛出异常")
    void getCheckedData_shouldThrowOnError() {
        CommonResult<Void> result = CommonResult.error(400, "错误");
        assertThrows(RuntimeException.class, result::getCheckedData);
    }

    @Test
    @DisplayName("error(ErrorCode, params) 应格式化消息")
    void error_withErrorCodeAndParams_shouldFormatMessage() {
        ErrorCode errorCode = new ErrorCode(400, "用户 %s 不存在");
        CommonResult<Void> result = CommonResult.error(errorCode, "张三");
        assertEquals(400, result.getCode());
        assertEquals("用户 张三 不存在", result.getMessage());
    }

    @Test
    @DisplayName("error(CommonResult) 应复制code和message")
    void error_withCommonResult_shouldCopyCodeAndMessage() {
        CommonResult<String> original = CommonResult.badRequest("原始错误");
        CommonResult<Void> copied = CommonResult.error(original);
        assertEquals(original.getCode(), copied.getCode());
        assertEquals(original.getMessage(), copied.getMessage());
        assertNull(copied.getData());
    }
}
