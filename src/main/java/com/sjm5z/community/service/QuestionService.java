package com.sjm5z.community.service;

import com.sjm5z.community.dto.PaginationDTO;
import com.sjm5z.community.dto.QuestionDTO;
import com.sjm5z.community.model.Question;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface QuestionService {
    //插入元素
    void insert(Question question, HttpSession session);

    //查询所有
    PaginationDTO findAll(String search,Integer page, Integer size);

    //更加userAccountID来查询
    PaginationDTO list(String search,Long id, Integer page, Integer size);

    //更具ID来查询文章和作者
    QuestionDTO getById(Long id);

    //更新或者添加提问
    void insertOrUpdate(Question question, HttpSession session);

    //添加阅读数
    void incView(Long id);

    //搜索此问题的相关标签
    List<QuestionDTO> selectRelate(QuestionDTO questionDTO);
}
