package com.joy.user.exception;

import com.joy.user.enums.ResultEnum;

/**
 * Created by SongLiang on 2019-08-10
 */
public class UserException extends RuntimeException {

    private Integer code;

    public UserException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public UserException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

}
