package com.sjm5z.community.controller;

import com.sjm5z.community.service.AuthorizationLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class AuthorizeController {

    @Autowired
    private AuthorizationLoginService authorizationLogin;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request, HttpServletResponse response) {
        //登录授权操作
        Cookie cookie = authorizationLogin.authorizationLogin(code, state, request.getSession());
        if(cookie != null){
            response.addCookie(cookie);
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        HttpSession session = request.getSession();
        Cookie logout = authorizationLogin.logout(session);
        response.addCookie(logout);
        return "redirect:/";
    }
}
