package com.joy.user.controller;

import com.joy.thrift.user.UserInfo;
import com.joy.thrift.user.dto.UserDTO;
import com.joy.user.converter.UserInfo2UserDTOConverter;
import com.joy.user.enums.ResultEnum;
import com.joy.user.exception.UserException;
import com.joy.user.redis.RedisClient;
import com.joy.user.response.LoginResponse;
import com.joy.user.response.Response;
import com.joy.user.thrift.ServiceProvider;
import com.joy.user.utils.JsonUtil;
import com.joy.user.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.thrift.TException;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.util.Random;

/**
 * Created by SongLiang on 2019-08-09
 */
@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ServiceProvider serviceProvider;

    @Autowired
    private RedisClient redisClient;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Response login(@RequestParam("username") String username,
                          @RequestParam("password") String password) {
        // 1. 验证用户密码
        UserInfo userInfo = null;
        log.info("username:{}, password:{}", username, password);
        try {
            userInfo = serviceProvider.getUserService().getUserByName(username);
        } catch (TException e) {
            e.printStackTrace();
            return ResponseUtil.error(ResultEnum.USERNAME_PASSWORD_INVALID);
        }
        if (userInfo == null ||
                !userInfo.getPassword().equalsIgnoreCase(md5(password))) {
            return ResponseUtil.error(ResultEnum.USERNAME_PASSWORD_INVALID);
        }

        // 2. 生成 token
        String token = genToken();

        // 3. 缓存用户
        redisClient.set(token, JsonUtil.toJson(UserInfo2UserDTOConverter.convert(userInfo)), 3600);
        return new LoginResponse(token);
    }

    @RequestMapping(value = "/sendVerifyCode", method = RequestMethod.POST)
    @ResponseBody
    public Response sendVerifyCode(@RequestParam(value = "mobile", required = false) String mobile,
                                   @RequestParam(value = "email", required = false) String email) {
        String message = "verify code is: ";
        String code = randomCode("0123456789", 6);

        try {
            boolean result = false;
            if (StringUtils.isNotBlank(mobile)) {
                result = serviceProvider.getMessageService().sendMobileMessage(mobile, message+code);
                redisClient.set(mobile, code);
            } else if (StringUtils.isNotBlank(email)) {
                result = serviceProvider.getMessageService().sendEmailMessage(email, message+code);
                redisClient.set(email, code);
            } else {
                return ResponseUtil.error(ResultEnum.MOBILE_OR_EMAIL_REQUIRED);
            }

            if (!result) {
                return ResponseUtil.error(ResultEnum.SEND_VERIFYCODE_FAILED);
            }
        } catch (TException e) {
            e.printStackTrace();
            throw new UserException(ResultEnum.SEND_MOBILE_OR_EMAIL_ERROR);
        }
        return ResponseUtil.success();
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Response register(@RequestParam("username") String username,
                             @RequestParam("password") String password,
                             @RequestParam(value = "mobile", required = false) String mobile,
                             @RequestParam(value = "email", required = false) String email,
                             @RequestParam("verifyCode") String verifyCode) {
        if (StringUtils.isBlank(mobile) && StringUtils.isBlank(email)) {
            return ResponseUtil.error(ResultEnum.MOBILE_OR_EMAIL_REQUIRED);
        }

        // 验证码是否通过
        String redisCode = StringUtils.isNotBlank(mobile) ? redisClient.get(mobile) : redisClient.get(email);
        log.info("redisCode:{}", redisCode);
        log.info("verifyCode:{}", verifyCode);
        if (!verifyCode.equals(redisCode)) {
            return ResponseUtil.error(ResultEnum.VERIFY_CODE_INVALID);
        }

        // 通过验证，保存用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(md5(password));
        userInfo.setMobile(mobile);
        userInfo.setEmail(email);
        try {
            serviceProvider.getUserService().registerUser(userInfo);
        } catch (TException e) {
            e.printStackTrace();
            throw new UserException(ResultEnum.REGISTER_FAILED);
        }

        return ResponseUtil.success();
    }

    @RequestMapping(value = "/authentication", method = RequestMethod.POST)
    @ResponseBody
    public UserDTO authentication(@RequestHeader("token") String token) {
        log.info("begin authentication");
        UserDTO userDTO = (UserDTO) JsonUtil.fromJson(redisClient.get(token), UserDTO.class);
        System.out.println("authentication");
        return userDTO;
    }

    private String genToken() {
        return randomCode("0123456789abcdefghijklmnopqrstuvwxyz", 32);
    }

    private String randomCode(String s, int size) {
        StringBuilder result = new StringBuilder(size);
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int loc = random.nextInt(s.length());
            result.append(s.charAt(loc));
        }
        return result.toString();
    }

    private String md5(String password) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(password.getBytes("utf-8"));
            return HexUtils.toHexString(md5Bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
