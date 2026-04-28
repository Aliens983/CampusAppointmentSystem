package com.laoliu.system.common.exception.enums;

import com.laoliu.system.common.exception.ErrorCode;

/**
 * 业务错误码枚举
 * 
 * @author 25516
 */
public interface BusinessErrorCode {

    // 用户相关错误码 1000-1099
    ErrorCode USER_NOT_FOUND = new ErrorCode(1001, "用户不存在");
    ErrorCode USER_ALREADY_EXISTS = new ErrorCode(1002, "用户已存在");
    ErrorCode USER_DISABLED = new ErrorCode(1003, "用户已被禁用");
    ErrorCode USER_LOGIN_FAILED = new ErrorCode(1004, "登录失败，用户名或密码错误");
    ErrorCode USER_TOKEN_EXPIRED = new ErrorCode(1005, "Token已过期");
    ErrorCode USER_TOKEN_INVALID = new ErrorCode(1006, "Token无效");

    // 认证授权相关错误码 1100-1199
    ErrorCode AUTHENTICATION_FAILED = new ErrorCode(1101, "认证失败");
    ErrorCode AUTHORIZATION_FAILED = new ErrorCode(1102, "权限不足");
    ErrorCode TOKEN_MISSING = new ErrorCode(1103, "缺少Token");
    ErrorCode TOKEN_INVALID_FORMAT = new ErrorCode(1104, "Token格式错误");

    // 验证码相关错误码 1200-1299
    ErrorCode VERIFICATION_CODE_EXPIRED = new ErrorCode(1201, "验证码已过期");
    ErrorCode VERIFICATION_CODE_INVALID = new ErrorCode(1202, "验证码错误");
    ErrorCode VERIFICATION_CODE_SEND_FAILED = new ErrorCode(1203, "验证码发送失败");

    // 预约相关错误码 1300-1399
    ErrorCode SERVICE_NOT_FOUND = new ErrorCode(1301, "服务不存在");
    ErrorCode SERVICE_NOT_AVAILABLE = new ErrorCode(1302, "服务暂不可用");
    ErrorCode BOOKING_FAILED = new ErrorCode(1303, "预约失败");
    ErrorCode BOOKING_NOT_FOUND = new ErrorCode(1304, "预约记录不存在");
    ErrorCode BOOKING_CANCEL_FAILED = new ErrorCode(1305, "取消预约失败");
    ErrorCode BOOKING_TIME_CONFLICT = new ErrorCode(1306, "预约时间冲突");

    // 文件相关错误码 1400-1499
    ErrorCode FILE_EMPTY = new ErrorCode(1400, "文件不能为空");
    ErrorCode FILE_UPLOAD_FAILED = new ErrorCode(1401, "文件上传失败");
    ErrorCode FILE_NOT_FOUND = new ErrorCode(1402, "文件不存在");
    ErrorCode FILE_SIZE_EXCEEDED = new ErrorCode(1403, "文件大小超出限制");
    ErrorCode FILE_TYPE_NOT_ALLOWED = new ErrorCode(1404, "不支持的文件类型");

    // 系统相关错误码 1500-1599
    ErrorCode SYSTEM_ERROR = new ErrorCode(1501, "系统错误");
    ErrorCode CACHE_ERROR = new ErrorCode(1502, "缓存操作失败");
    ErrorCode MESSAGE_ERROR = new ErrorCode(1503, "消息发送失败");
    ErrorCode CONFIG_ERROR = new ErrorCode(1504, "配置错误");

}