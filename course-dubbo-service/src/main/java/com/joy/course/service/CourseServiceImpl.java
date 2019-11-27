package com.joy.course.service;

import com.joy.course.converter.UserInfo2TeacherDTOConverter;
import com.joy.course.dto.CourseDTO;
import com.joy.course.mapper.CourseMapper;
import com.joy.thrift.user.UserInfo;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import java.util.List;

/**
 * Created by SongLiang on 2019-08-11
 */
@Service
public class CourseServiceImpl implements ICourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ServiceProvider serviceProvider;

    @Override
    public List<CourseDTO> courseList() {
        List<CourseDTO> courseDTOS = courseMapper.listCourse();

        if (courseDTOS != null) {
            for (CourseDTO courseDTO : courseDTOS) {
                Integer teacherId = courseMapper.getCourseTeacher(courseDTO.getId());
                if (teacherId != null) {
                    try {
                        UserInfo userInfo = serviceProvider.getUserService().getTeacherbyId(teacherId);
                        courseDTO.setTeacher(UserInfo2TeacherDTOConverter.convert(userInfo));
                    } catch (TException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return courseDTOS;
    }

}
