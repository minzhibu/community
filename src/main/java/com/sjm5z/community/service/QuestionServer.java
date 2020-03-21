package com.sjm5z.community.service;

import com.sjm5z.community.dto.PaginationDTO;
import com.sjm5z.community.model.Question;

import javax.servlet.http.HttpSession;

public interface QuestionServer {
    //插入元素
    void insert(Question question, HttpSession session);

    //查询所有
    PaginationDTO findAll(Integer page, Integer size);
}
