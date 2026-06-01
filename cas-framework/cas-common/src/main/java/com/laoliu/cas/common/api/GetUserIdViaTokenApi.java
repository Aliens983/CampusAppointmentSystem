package com.laoliu.cas.common.api;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author forever-king
 */
public interface GetUserIdViaTokenApi {
    Long getUserId(HttpServletRequest request);
}
