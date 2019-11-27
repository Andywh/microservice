package com.joy.course.filter;

import com.joy.thrift.user.dto.UserDTO;
import com.joy.user.client.LoginFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by SongLiang on 2019-08-11
 */
@Slf4j
public class CourseFilter extends LoginFilter {

    @Override
    protected void login(HttpServletRequest request, HttpServletResponse response, UserDTO userDTO) {
        log.info("login...");
        request.setAttribute("user", userDTO);
        log.info("after login...");
    }
}
