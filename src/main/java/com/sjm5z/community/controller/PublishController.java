package com.sjm5z.community.controller;

import com.sjm5z.community.model.Question;
import com.sjm5z.community.service.AuthorizationLoginService;
import com.sjm5z.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class PublishController {

    @Autowired
    private QuestionService questionServer;
    @Autowired
    private AuthorizationLoginService authorizationLoginServer;

    @GetMapping("/publish")
    public String publish(HttpServletRequest request, HttpServletResponse response){
//        //判断用户之前是否登录过
//        Cookie cookie = authorizationLoginServer.getTokenToUser(request.getCookies(), request.getSession());
//        if (cookie != null) {
//            response.addCookie(cookie);
//        }
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(Question question, HttpServletRequest request) {
        System.out.println(question);
        questionServer.insert(question,request.getSession());
        System.out.println(question);
        return "redirect:/";
    }
}
