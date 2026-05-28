package com.laoliu.system.common.exception.enums;

import com.laoliu.system.common.exception.ErrorCode;

public interface BookErrorCode {

    ErrorCode BOOKING_NOT_FOUND = new ErrorCode(404, "预约不存在");

    ErrorCode BOOKING_CANCEL_FAILED = new ErrorCode(400, "取消预约失败");

    ErrorCode BOOKING_FAILED = new ErrorCode(400, "预约失败");

    ErrorCode SERVICE_NOT_AVAILABLE = new ErrorCode(400, "服务不可用");

}
