package com.sjm5z.community.interceptor;

import com.sjm5z.community.model.User;
import com.sjm5z.community.service.AuthorizationLoginService;
import com.sjm5z.community.service.impl.NotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class SessionInterception implements HandlerInterceptor {


    @Autowired
    private AuthorizationLoginService authorizationLoginServer;

    @Autowired
    private NotificationServiceImpl notificationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断用户之前是否登录过
        Cookie cookie = authorizationLoginServer.getTokenToUser(request.getCookies(), request.getSession());
        if (cookie != null) {
            response.addCookie(cookie);
        }
        //通知个数存放在session中
        HttpSession session = request.getSession();
        if((User) session.getAttribute("user") != null){
            Long unreadCount = notificationService.unreadCount(((User) session.getAttribute("user")).getId());
            session.setAttribute("unreadCount",unreadCount);
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
