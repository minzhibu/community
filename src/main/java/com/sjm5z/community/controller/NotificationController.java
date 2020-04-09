package com.sjm5z.community.controller;

import com.sjm5z.community.dto.NotificationDTO;
import com.sjm5z.community.model.User;
import com.sjm5z.community.service.impl.NotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.sjm5z.community.enums.NotificationTypeEnum;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Autowired
    private NotificationServiceImpl notificationService;

    @GetMapping("/notification/{id}")
    public String profile(@PathVariable(name="id") Long id,HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return "redirect:/";
        }
        NotificationDTO notificationDTO = notificationService.read(id,user);
        if(NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDTO.getType()
            ||NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType()){
            return "redirect:/question/"+ notificationDTO.getOuterid();
        }else{
            return "redirect:/";
        }
    }
}
