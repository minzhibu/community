package com.sjm5z.community.service.impl;

import com.sjm5z.community.dto.PaginationDTO;
import com.sjm5z.community.dto.QuestionDTO;
import com.sjm5z.community.mapper.QuestionMapper;
import com.sjm5z.community.model.Question;
import com.sjm5z.community.model.User;
import com.sjm5z.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public void insert(Question question, HttpSession session) {
        User user = (User) session.getAttribute("user");
        System.out.println(user);
        question.setUserID(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        System.out.println(question);
        questionMapper.insert(question);
    }

    @Override
    public PaginationDTO findAll(Integer page, Integer size) {
        return findByAccountId(null, page, size);
    }

    @Override
    public PaginationDTO findByAccountId(Integer id, Integer page, Integer size) {
        if(size < 2 || size > 20){
            size = 7;
        }
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount;
        if (id != null) {
            totalCount = questionMapper.countByAccountId(id);
        } else {
            totalCount = questionMapper.count();
        }
        if(totalCount != 0){
            //初始分页对象的属性
            paginationDTO.setPagination(totalCount, page, size);
            List<QuestionDTO> questionList;
            //获取总记录数
            Integer page1 = paginationDTO.getPage();
            Integer offset = size * (page1 - 1);
            if (id != null) {
                questionList = questionMapper.findUserToQuestion(id, offset, size);
            } else {
                questionList = questionMapper.findAllAndUser(offset, size);
            }
            paginationDTO.setQuestions(questionList);
            return paginationDTO;
        }
        return null;
    }

    @Override
    public QuestionDTO getById(Integer id) {
        return questionMapper.findById(id);
    }
}
