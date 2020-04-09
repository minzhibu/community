package com.sjm5z.community.controller;

import com.sjm5z.community.common.StringCommon;
import com.sjm5z.community.dto.PaginationDTO;
import com.sjm5z.community.model.User;
import com.sjm5z.community.service.QuestionService;
import com.sjm5z.community.service.impl.NotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class profileController {

    @Autowired
    private QuestionService questionServer;

    @Autowired
    private NotificationServiceImpl notificationService;



    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          @RequestParam(name = "page", defaultValue = "1") String page,
                          @RequestParam(name = "size", defaultValue = "7") String size,
                          Model model,
                          HttpServletRequest request) {
        HttpSession session = request.getSession();
        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
            PaginationDTO paginationDTO = questionServer.list(null,((User) session.getAttribute("user")).getId(), StringCommon.StringToInteger(page),
                    StringCommon.StringToInteger(size));
            model.addAttribute("myQuestions", paginationDTO);
        } else if ("replies".equals(action)) {
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
            PaginationDTO paginationDTO = notificationService.list(((User) session.getAttribute("user")).getId(), StringCommon.StringToInteger(page),
                    StringCommon.StringToInteger(size));
            model.addAttribute("myQuestions", paginationDTO);
        }
        return "profile";
    }


}
