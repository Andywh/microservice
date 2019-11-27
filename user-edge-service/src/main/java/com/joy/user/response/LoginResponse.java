package com.joy.user.response;

import lombok.Data;

/**
 * Created by SongLiang on 2019-08-09
 */
@Data
public class LoginResponse extends Response {

    private String token;

    public LoginResponse(String token) {
        super.setCode(0);
        super.setMsg("成功");
        this.token = token;
    }

}
