package com.sjm5z.community.controller;

import com.sjm5z.community.server.AuthorizationLoginServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AuthorizeController {

    @Autowired
    private AuthorizationLoginServer authorizationLogin;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request, HttpServletResponse response) {
        //登录授权操作
        Cookie cookie = authorizationLogin.authorizationLogin(code, state, request.getSession());
        System.out.println(cookie);
        if(cookie != null){
            response.addCookie(cookie);
        }
        return "redirect:/";
    }
}
