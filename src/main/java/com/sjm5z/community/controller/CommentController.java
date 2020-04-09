package com.sjm5z.community.controller;

import com.sjm5z.community.dto.CommentCreateDTO;
import com.sjm5z.community.dto.ResultDTO;
import com.sjm5z.community.exception.CustomizeErrorCode;
import com.sjm5z.community.model.User;
import com.sjm5z.community.service.impl.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {

    @Autowired
    private CommentServiceImpl commentService;


    //添加评论
    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        return commentService.insert(commentCreateDTO,user);
    }
}
