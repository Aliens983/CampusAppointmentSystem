package com.laoliu.system.common;

import lombok.Getter;

/**
 * @author 25516
 */

public enum ServiceStatus {
    ONLINE(1, "已开启"),
    CLOSED(0, "已关闭");

    @Getter
    private final int code;
    @Getter
    private final String message;

    ServiceStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
