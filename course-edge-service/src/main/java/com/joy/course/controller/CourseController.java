package com.joy.course.controller;

import com.joy.course.dto.CourseDTO;
import com.joy.course.service.ICourseService;
import com.joy.thrift.user.dto.UserDTO;
import com.alibaba.dubbo.config.annotation.Reference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by SongLiang on 2019-08-11
 */
@Controller
@RequestMapping("/course")
@Slf4j
public class CourseController {

    @Reference
    private ICourseService courseService;

    @RequestMapping(value = "/courseList", method = RequestMethod.GET)
    @ResponseBody
    public List<CourseDTO> courseList(HttpServletRequest request) {
        UserDTO user = (UserDTO)request.getAttribute("user");
        return courseService.courseList();
    }

}
