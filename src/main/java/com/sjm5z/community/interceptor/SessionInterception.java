package com.sjm5z.community.interceptor;

import com.sjm5z.community.service.AuthorizationLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SessionInterception implements HandlerInterceptor {


    @Autowired
    private AuthorizationLoginService authorizationLoginServer;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断用户之前是否登录过
        Cookie cookie = authorizationLoginServer.getTokenToUser(request.getCookies(), request.getSession());
        if (cookie != null) {
            response.addCookie(cookie);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
