package com.laoliu.system.api;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author 25516
 */
public interface GetUserIdViaTokenApi {

    Long getUserId(HttpServletRequest request);

}
