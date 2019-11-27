package com.joy.course.converter;

import com.joy.thrift.user.UserInfo;
import com.joy.thrift.user.dto.TeacherDTO;
import org.springframework.beans.BeanUtils;

/**
 * Created by SongLiang on 2019-08-11
 */
public class UserInfo2TeacherDTOConverter {

    public static TeacherDTO convert(UserInfo userInfo) {
        TeacherDTO teacherDTO = new TeacherDTO();
        BeanUtils.copyProperties(userInfo, teacherDTO);
        System.out.println("123");
        return teacherDTO;
    }

}
