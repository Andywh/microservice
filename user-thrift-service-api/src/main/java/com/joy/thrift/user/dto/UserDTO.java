package com.joy.thrift.user.dto;

import lombok.Data;

/**
 * Created by SongLiang on 2019-08-09
 */
@Data
public class UserDTO {

    private int id;

    private String username;

    private String password;

    private String mobile;

    private String email;

    private String realName;

}
