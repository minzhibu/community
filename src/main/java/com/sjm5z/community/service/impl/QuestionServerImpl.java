package com.sjm5z.community.service.impl;

import com.sjm5z.community.dto.GitHubUser;
import com.sjm5z.community.dto.PaginationDTO;
import com.sjm5z.community.dto.QuestionDTO;
import com.sjm5z.community.mapper.QuestionMapper;
import com.sjm5z.community.model.Question;
import com.sjm5z.community.service.QuestionServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class QuestionServerImpl implements QuestionServer {
    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public void insert(Question question, HttpSession session) {
        GitHubUser gitHubUser = (GitHubUser) session.getAttribute("user");
        System.out.println(gitHubUser);
        question.setCreator(gitHubUser.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        System.out.println(question);
        questionMapper.insert(question);
    }

    @Override
    public PaginationDTO findAll(Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer offset = size * (page - 1);
        //获取总记录数
        Integer totalCount = questionMapper.count();
        //初始分页对象的属性
        paginationDTO.setPagination(totalCount, page, size);
        List<QuestionDTO> questionList = questionMapper.findAllAndUser(paginationDTO.getPage(), size);
        paginationDTO.setQuestions(questionList);
        return paginationDTO;
    }
}
