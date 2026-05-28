package com.laoliu.system.common.exception.enums;

import com.laoliu.system.common.exception.ErrorCode;

/**
 * @author forever-king
 */
public interface ServiceErrorCode {

    ErrorCode SERVICE_NOT_FOUND = new ErrorCode(404, "服务不存在");

    ErrorCode SERVICE_ALREADY_BOOKED = new ErrorCode(409, "服务已预约");

    ErrorCode SERVICE_BOOK_FAILED = new ErrorCode(400, "服务预约失败");

    ErrorCode SERVICE_CANCEL_FAILED = new ErrorCode(400, "取消预约失败");

    ErrorCode SERVICE_NOT_ENABLED = new ErrorCode(400, "服务未启用");

}
