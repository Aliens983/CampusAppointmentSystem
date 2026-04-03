package com.laoliu.system.common.exception.enums;

import com.laoliu.system.common.exception.ErrorCode;

/**
 * @author 25516
 */
public interface LoginErrorCode {

    ErrorCode USER_NOT_EXIST = new ErrorCode(404, "用户不存在");

}
