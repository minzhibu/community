package com.sjm5z.community.controller;

import com.sjm5z.community.model.Question;
import com.sjm5z.community.service.QuestionServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionServer questionServer;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(Question question, HttpServletRequest request) {
        questionServer.insert(question,request.getSession());
        System.out.println(question);
        return "redirect:/";
    }
}
