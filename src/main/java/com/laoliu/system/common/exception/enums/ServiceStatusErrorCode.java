package com.laoliu.system.common.exception.enums;

import com.laoliu.system.common.exception.ErrorCode;

/**
 * @author forever-king
 */
public interface ServiceStatusErrorCode {

    ErrorCode STATUS_NOT_FOUND = new ErrorCode(404, "状态不存在");

    ErrorCode AUDIT_FAILED = new ErrorCode(400, "审核失败");

    ErrorCode AUDIT_REASON_REQUIRED = new ErrorCode(3838438, "拒绝原因不能为空");

    ErrorCode INVALID_AUDIT_STATUS = new ErrorCode(400, "无效的审核状态");

}
