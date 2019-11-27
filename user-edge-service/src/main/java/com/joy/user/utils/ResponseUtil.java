package com.joy.user.utils;

import com.joy.user.enums.ResultEnum;
import com.joy.user.response.Response;

/**
 * Created by SongLiang on 2019-08-09
 */
public class ResponseUtil {

    public static Response success() {
        Response response = new Response();
        response.setCode(0);
        response.setMsg("成功");
        return response;
    }

    public static Response success(Object object) {
        Response response = new Response();
        response.setCode(0);
        response.setMsg("成功");
        response.setData(object);
        return response;
    }

    public static Response error(ResultEnum resultEnum) {
        Response response = new Response();
        response.setCode(resultEnum.getCode());
        response.setMsg(resultEnum.getMessage());
        return response;
    }

}
