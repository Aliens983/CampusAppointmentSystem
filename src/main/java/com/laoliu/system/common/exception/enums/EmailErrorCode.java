package com.laoliu.system.common.exception.enums;

import com.laoliu.system.common.exception.ErrorCode;

/**
 * @author forever-king
 */
public interface EmailErrorCode {

    ErrorCode EMAIL_NOT_PROVIDED = new ErrorCode(400, "邮箱不能为空");

    ErrorCode EMAIL_FORMAT_ERROR = new ErrorCode(400, "邮箱格式错误");

    ErrorCode EMAIL_SEND_FAILED = new ErrorCode(500, "邮件发送失败");

    ErrorCode EMAIL_SEND_TOO_FREQUENTLY = new ErrorCode(429, "邮件发送过于频繁");

    ErrorCode VERIFICATION_CODE_EXPIRED = new ErrorCode(401, "验证码已过期");

    ErrorCode VERIFICATION_CODE_ERROR = new ErrorCode(401, "验证码错误");

    ErrorCode VERIFICATION_CODE_NOT_FOUND = new ErrorCode(404, "验证码不存在");

}
