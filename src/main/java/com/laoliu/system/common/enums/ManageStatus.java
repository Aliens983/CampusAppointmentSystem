package com.laoliu.system.common.enums;

import lombok.Getter;

/**
 * @author 25516
 */

public enum ManageStatus {
    SUBMIT(0, "已提交,待审核"),
    APPROVED(1, "审核通过"),
    REJECTED(2, "审核未通过");

    @Getter
    private final int code;

    @Getter
    private final String message;

    ManageStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
