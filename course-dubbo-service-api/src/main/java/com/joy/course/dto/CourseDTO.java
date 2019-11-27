package com.joy.course.dto;

import com.joy.thrift.user.dto.TeacherDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by SongLiang on 2019-08-11
 */
@Data
public class CourseDTO implements Serializable {

    private int id;

    private String title;

    private String description;

    private TeacherDTO teacher;

}
