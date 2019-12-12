package com.joy.user.client;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.joy.thrift.user.dto.UserDTO;
import com.joy.user.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Created by SongLiang on 2019-08-10
 */
@Slf4j
public abstract class LoginFilter implements Filter {

    private static Cache<String, UserDTO> cache =
            CacheBuilder.newBuilder().maximumSize(10000).expireAfterWrite(3, TimeUnit.MINUTES).build();

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        String token = request.getParameter("token");
        log.info("token1: {}", token);
        if (StringUtils.isBlank(token)) {
            log.info("token1: search from cookie");
            Cookie cookie = CookieUtil.get(request, "token");
            if (cookie != null) {
                token = cookie.getValue();
            }
        }

        log.info("token2: {}", token);
        log.info("test");
        UserDTO userDTO = null;
        if (StringUtils.isNotBlank(token)) {
            log.info("before cache get");
            userDTO = cache.getIfPresent(token);
            log.info("after cache get");
            if (userDTO == null) {
                userDTO = requestUserInfo(token);
                if (userDTO != null) {
                    cache.put(token, userDTO);
                }
            }
        }

        if (userDTO == null) {
            log.info("userDTO==null");
            response.sendRedirect("http://www.joy.com/user/login");
//            response.sendRedirect("http://user-edge-service:8082/user/login");
            return;
        }

        log.info("before login");
        log.info("userDTO:{}", userDTO);
        login(request, response, userDTO);

        filterChain.doFilter(request, response);

    }

    protected abstract void login(HttpServletRequest request, HttpServletResponse response, UserDTO userDTO);

    private UserDTO requestUserInfo(String token) {
//        String url = "http://127.0.0.1:8082/user/authentication";
//        HttpClient client = new DefaultHttpClient();
//        HttpPost post = new HttpPost();
//        post.addHeader("token", token);
//        InputStream inputStream = null;
//        try {
//            HttpResponse response = client.execute(post);
//            log.info("try error");
//            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
//                throw new RuntimeException("request user info failed! StatusLine:" + response.getStatusLine());
//            }
//            inputStream = response.getEntity().getContent();
//            byte[] temp = new byte[1024];
//            StringBuilder sb = new StringBuilder();
//            int len = 0;
//            while ((len = inputStream.read(temp)) > 0) {
//                sb.append(new String(temp, len));
//            }
//            UserDTO userDTO = new ObjectMapper().readValue(sb.toString(), UserDTO.class);
//            return userDTO;
//        } catch (IOException e) {
//            log.info("IOException");
//
//            e.printStackTrace();
//        } finally {
//            if (inputStream != null) {
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return null;
        String url = "http://www.joy.com/user/authentication";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        post.addHeader("token", token);
        InputStream inputStream = null;
        log.info("begin post");
        try {
            HttpResponse response = client.execute(post);
            if(response.getStatusLine().getStatusCode()!= HttpStatus.SC_OK) {
                throw new RuntimeException("request user info failed! StatusLine:"+response.getStatusLine());
            }
            inputStream = response.getEntity().getContent();
            byte[] temp = new byte[1024];
            StringBuilder sb = new StringBuilder();
            int len = 0;
            while((len = inputStream.read(temp))>0) {
                sb.append(new String(temp,0,len));
            }
            log.info("userinfo: {}", sb.toString());

            UserDTO userDTO = new ObjectMapper().readValue(sb.toString(), UserDTO.class);
            return userDTO;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(inputStream!=null) {
                try{
                    inputStream.close();
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }

    public void destroy() {

    }

}
