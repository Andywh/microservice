package com.joy.user.enums;

import lombok.Getter;

/**
 * Created by SongLiang on 2019-08-09
 */
@Getter
public enum ResultEnum {

    USERNAME_PASSWORD_INVALID(1001, "username or password is error"),
    MOBILE_OR_EMAIL_REQUIRED(1002, "mobile or email is required"),
    SEND_VERIFYCODE_FAILED(1003, "send verifycode failed"),
    VERIFY_CODE_INVALID(1004, "verify code invalid"),

    SEND_MOBILE_OR_EMAIL_ERROR(2001, "send mobile or email error"),
    REGISTER_FAILED(2002, "保存用户信息失败"),
    ;

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
