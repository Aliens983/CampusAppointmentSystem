package com.laoliu.system.common.exception.enums;

import com.laoliu.system.common.exception.ErrorCode;

/**
 * @author forever-king
 */

public interface CommonErrorCode {

    ErrorCode SUCCESS = new ErrorCode(200, "操作成功");

    ErrorCode INTERNAL_SERVER_ERROR = new ErrorCode(500, "服务器内部错误");

    ErrorCode BAD_REQUEST = new ErrorCode(400, "请求参数错误");

    ErrorCode UNAUTHORIZED = new ErrorCode(401, "未授权");

    ErrorCode FORBIDDEN = new ErrorCode(403, "禁止访问");

    ErrorCode NOT_FOUND = new ErrorCode(404, "资源不存在");

}
