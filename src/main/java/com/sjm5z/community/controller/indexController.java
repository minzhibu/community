package com.sjm5z.community.controller;

import com.sjm5z.community.common.StringCommon;
import com.sjm5z.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class indexController {

    @Autowired
    private QuestionService questionServer;

    @GetMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response, Model model,
                        @RequestParam(name = "page", defaultValue = "1") String page,
                        @RequestParam(name = "size", defaultValue = "7") String size,
                        @RequestParam(name = "search",required = false) String search
                        ) {
        model.addAttribute("pagination", questionServer.findAll(search,StringCommon.StringToInteger(page),
                StringCommon.StringToInteger(size)));
        model.addAttribute("search",search);
        return "index";
    }
}
