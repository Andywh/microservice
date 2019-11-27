package com.joy.user.converter;

import com.joy.thrift.user.UserInfo;
import com.joy.thrift.user.dto.UserDTO;
import org.springframework.beans.BeanUtils;

/**
 * Created by SongLiang on 2019-08-09
 */
public class UserInfo2UserDTOConverter {

    public static UserDTO convert(UserInfo userInfo) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userInfo, userDTO);
        return userDTO;
    }
}
