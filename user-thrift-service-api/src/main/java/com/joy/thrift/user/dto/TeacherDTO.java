package com.joy.thrift.user.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by SongLiang on 2019-08-11
 */
@Data
public class TeacherDTO extends UserDTO implements Serializable {

    private String introduction;

    private int stars;

}
