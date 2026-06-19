package com.laoliu.cas.common.exception.code;

import com.laoliu.cas.common.exception.ErrorCode;

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

    ErrorCode TOKEN_EXPIRED = new ErrorCode(401, "Token已过期");

    ErrorCode TOKEN_INVALID = new ErrorCode(401, "Token格式错误或无效");

    ErrorCode FILE_UPLOAD_FAILED = new ErrorCode(500, "文件上传失败");

    ErrorCode SMS_SEND_FAILED = new ErrorCode(500, "短信发送失败");

    ErrorCode EMAIL_SEND_FAILED = new ErrorCode(500, "邮件发送失败");

    ErrorCode QR_CODE_FAILED = new ErrorCode(500, "二维码生成失败");

    ErrorCode WEATHER_QUERY_FAILED = new ErrorCode(500, "天气查询失败");

    ErrorCode FILE_EMPTY = new ErrorCode(400, "文件不能为空");

}
