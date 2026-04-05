package com.laoliu.system.common.exception.enums;

import com.laoliu.system.common.exception.ErrorCode;

/**
 * @author forever-king
 */
public interface RoleErrorCode {

    ErrorCode ROLE_NOT_FOUND = new ErrorCode(404, "角色不存在");

    ErrorCode ROLE_UPDATE_FAILED = new ErrorCode(400, "角色更新失败");

    ErrorCode PERMISSION_DENIED = new ErrorCode(403, "权限不足");

    ErrorCode ADMIN_ROLE_REQUIRED = new ErrorCode(403, "需要管理员权限");

}
