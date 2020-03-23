package com.sjm5z.community.controller;

import com.sjm5z.community.common.StringCommon;
import com.sjm5z.community.dto.PaginationDTO;
import com.sjm5z.community.model.User;
import com.sjm5z.community.service.QuestionService;
import com.sjm5z.community.service.impl.AuthorizationLoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class profileController {

    @Autowired
    private AuthorizationLoginServiceImpl authorizationLoginServer;
    @Autowired
    private QuestionService questionServer;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          @RequestParam(name = "page", defaultValue = "1") String page,
                          @RequestParam(name = "size", defaultValue = "7") String size,
                          Model model,
                          HttpServletRequest request,
                          HttpServletResponse response) {
        //判断用户之前是否登录过
        HttpSession session = request.getSession();
//        Cookie cookie = authorizationLoginServer.getTokenToUser(request.getCookies(), session);
//        if (cookie != null) {
//            response.addCookie(cookie);
//        }
        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
            PaginationDTO paginationDTO = questionServer.findByAccountId(((User) session.getAttribute("user")).getId(), StringCommon.StringToInteger(page),
                    StringCommon.StringToInteger(size));
            System.out.println(paginationDTO);
            model.addAttribute("myQuestions", paginationDTO);
        } else if ("replies".equals(action)) {
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
        }

        return "profile";
    }
}
