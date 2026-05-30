package com.laoliu.cas.common.api;

import jakarta.servlet.http.HttpServletRequest;

public interface GetUserIdViaTokenApi {
    Long getUserId(HttpServletRequest request);
}
