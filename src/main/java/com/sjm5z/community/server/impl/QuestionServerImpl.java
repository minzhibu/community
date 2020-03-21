package com.sjm5z.community.server.impl;

import com.sjm5z.community.dto.GitHubUser;
import com.sjm5z.community.mapper.QuestionMapper;
import com.sjm5z.community.model.Question;
import com.sjm5z.community.server.QuestionServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class QuestionServerImpl implements QuestionServer {
    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public void insert(Question question, HttpSession session) {
        GitHubUser gitHubUser = (GitHubUser)session.getAttribute("user");
        System.out.println(gitHubUser);
        question.setCreator(gitHubUser.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        System.out.println(question);
        questionMapper.insert(question);
    }
}
