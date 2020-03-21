package com.sjm5z.community.controller;

import com.sjm5z.community.server.impl.AuthorizationLoginServerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class indexController {

    @Autowired
    private AuthorizationLoginServerImpl authorizationLoginServer;

    @GetMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        //判断用户之前是否登录过
        Cookie cookie = authorizationLoginServer.getTokenToUser(request.getCookies(), request.getSession());
        if(cookie != null){
            response.addCookie(cookie);
        }
        return "index";
    }
}
