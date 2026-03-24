package com.laoliu.system.common;

import lombok.Getter;

/**
 * @author 25516
 */
public enum ManageStatus {
    SUBMIT(0, "已提交,待审核"),
    APPROVED(1, "审核通过"),
    REJECTED(2, "审核未通过"),
    CANCEL(3, "已撤销");

    @Getter
    private final Integer code;

    @Getter
    private final String message;

    ManageStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
