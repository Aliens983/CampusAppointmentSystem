package com.laoliu.cas.common.exception.code;

import com.laoliu.cas.common.exception.ErrorCode;

/**
 * 预约相关错误码
 *
 * @author forever-king
 */
public interface BookErrorCode {

    ErrorCode BOOKING_NOT_FOUND = new ErrorCode(10004, "预约不存在");

    ErrorCode BOOKING_CANCEL_FAILED = new ErrorCode(10010, "取消预约失败");

    ErrorCode BOOKING_FAILED = new ErrorCode(10004, "预约失败");

    ErrorCode SERVICE_NOT_AVAILABLE = new ErrorCode(10003, "服务不可用");

    // ========== BookServiceImpl 专用错误码 ==========

    ErrorCode SERVICE_ID_EMPTY = new ErrorCode(10001, "服务ID列表不能为空");

    ErrorCode SERVICE_NOT_EXIST = new ErrorCode(10002, "服务不存在: {0}");

    ErrorCode SERVICE_DISABLED = new ErrorCode(10003, "服务已被禁用: {0}");

    ErrorCode BOOK_FAILED = new ErrorCode(10004, "预约失败");

}
