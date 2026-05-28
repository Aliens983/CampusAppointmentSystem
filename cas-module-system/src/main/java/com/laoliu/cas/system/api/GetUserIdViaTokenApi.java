package com.laoliu.cas.system.api;

import jakarta.servlet.http.HttpServletRequest;

public interface GetUserIdViaTokenApi {
    Long getUserId(HttpServletRequest request);
}
