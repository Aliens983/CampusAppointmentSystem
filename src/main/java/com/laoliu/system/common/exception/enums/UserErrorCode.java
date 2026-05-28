package com.laoliu.system.common.exception.enums;

import com.laoliu.system.common.exception.ErrorCode;

/**
 * @author forever-king
 */
public interface UserErrorCode {

    ErrorCode USER_NOT_EXIST = new ErrorCode(404, "用户不存在");

    ErrorCode USER_ALREADY_EXISTS = new ErrorCode(409, "用户已存在");

    ErrorCode USER_INFO_ERROR = new ErrorCode(400, "用户信息错误");

    ErrorCode USER_ROLE_ERROR = new ErrorCode(403, "用户角色权限不足");

}
