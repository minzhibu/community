package com.sjm5z.community.controller;

import com.sjm5z.community.common.StringCommon;
import com.sjm5z.community.dto.QuestionDTO;
import com.sjm5z.community.enums.CommentTypeEnum;
import com.sjm5z.community.exception.CustomizeErrorCode;
import com.sjm5z.community.exception.CustomizeException;
import com.sjm5z.community.service.QuestionService;
import com.sjm5z.community.service.impl.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentServiceImpl commentService;

    //点击进来后增加阅读数
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id, Model model,
                           HttpServletRequest request
    ) {
        QuestionDTO questionDTO = questionService.getById(id);
        List<QuestionDTO> relatedQuestion = questionService.selectRelate(questionDTO);
        questionService.incView(questionDTO.getId());
        model.addAttribute("question", questionDTO);
        model.addAttribute("relatedQuestions",relatedQuestion);
        return "question";
    }

    //ajax请求来获取对于问题的评论
    @RequestMapping(value = "/questionPage", method = RequestMethod.GET)
    @ResponseBody
    public Object questionPage(@RequestParam(name = "id", defaultValue = "1") Long id,
                               @RequestParam(name = "type", defaultValue = "1") Integer type,
                               @RequestParam(name = "page", defaultValue = "1") String page,
                               @RequestParam(name = "size", defaultValue = "7") String size) {
        System.out.println(type);
        if(CommentTypeEnum.isExist(type)){
            return commentService.listByQuestionId(id, type,StringCommon.StringToInteger(size),
                    StringCommon.StringToInteger(page));
        }
        throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
    }


}
