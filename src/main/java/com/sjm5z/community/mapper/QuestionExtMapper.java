package com.sjm5z.community.mapper;

import com.sjm5z.community.dto.QuestionQueryDTO;
import com.sjm5z.community.model.Question;

import java.util.List;

public interface QuestionExtMapper {
    //增加阅读数
    int incView(Question record);

    //真假回复数
    int incCommentCount(Question record);

    //查询标签
    List<Question> selectRelated(Question question);

    //更具是否存在search来查询个数
    int countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}