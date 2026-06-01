package com.laoliu.cas.common.exception.code;

import com.laoliu.cas.common.exception.ErrorCode;

/**
 * @author forever-king
 */
public interface UserErrorCode {

    ErrorCode USER_NOT_EXIST = new ErrorCode(404, "用户不存在");

    ErrorCode USER_ALREADY_EXISTS = new ErrorCode(409, "用户已存在");

    ErrorCode USER_INFO_ERROR = new ErrorCode(400, "用户信息错误");

    ErrorCode USER_ROLE_ERROR = new ErrorCode(403, "用户角色权限不足");

    ErrorCode EMAIL_OR_CODE_EMPTY = new ErrorCode(400, "邮箱和验证码不能为空");

    ErrorCode VERIFICATION_CODE_EXPIRED = new ErrorCode(400, "验证码不存在或已过期");

    ErrorCode VERIFICATION_CODE_ERROR = new ErrorCode(400, "验证码错误");
}
