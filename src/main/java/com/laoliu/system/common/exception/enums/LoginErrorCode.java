package com.laoliu.system.common.exception.enums;

import com.laoliu.system.common.exception.ErrorCode;

/**
 * @author forever-king
 */
public interface LoginErrorCode {

    ErrorCode USER_NOT_EXIST = new ErrorCode(404, "用户不存在");

    ErrorCode PASSWORD_ERROR = new ErrorCode(401, "密码错误");

    ErrorCode LOGIN_FAILED = new ErrorCode(401, "登录失败");

    ErrorCode TOKEN_EXPIRED = new ErrorCode(401, "token已过期");

    ErrorCode TOKEN_INVALID = new ErrorCode(401, "token无效");

    ErrorCode VERIFICATION_CODE_ERROR = new ErrorCode(400, "验证码错误");

    ErrorCode VERIFICATION_CODE_EXPIRED = new ErrorCode(400, "验证码已过期");

    ErrorCode USER_NOT_EXIST_BY_EMAIL = new ErrorCode(404, "该邮箱未注册");

}
