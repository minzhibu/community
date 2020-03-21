package com.sjm5z.community.controller;

import com.sjm5z.community.common.StringCommon;
import com.sjm5z.community.service.QuestionServer;
import com.sjm5z.community.service.impl.AuthorizationLoginServerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class indexController {

    @Autowired
    private AuthorizationLoginServerImpl authorizationLoginServer;

    @Autowired
    private QuestionServer questionServer;

    @GetMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response, Model model,
                        @RequestParam(name = "page", defaultValue = "1") String page,
                        @RequestParam(name = "size", defaultValue = "7") String size) {

        //判断用户之前是否登录过
        Cookie cookie = authorizationLoginServer.getTokenToUser(request.getCookies(), request.getSession());
        if (cookie != null) {
            response.addCookie(cookie);
        }
        model.addAttribute("pagination", questionServer.findAll(StringCommon.StringToInteger(page),
                StringCommon.StringToInteger(size)));
        return "index";
    }
}
