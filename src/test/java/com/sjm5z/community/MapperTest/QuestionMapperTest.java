package com.sjm5z.community.MapperTest;

import com.sjm5z.community.dto.PaginationDTO;
import com.sjm5z.community.dto.QuestionQueryDTO;
import com.sjm5z.community.mapper.QuestionExtMapper;
import com.sjm5z.community.mapper.QuestionMapper;
import com.sjm5z.community.model.Question;
import com.sjm5z.community.service.impl.QuestionServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class QuestionMapperTest {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private QuestionServiceImpl questionService;

    @Test
    public void findAllAndUserTest() {
    }
    @Test
    void findByAccountIdTest(){
        PaginationDTO byAccountId = questionService.list(null,null, 1, 10);
        System.out.println(byAccountId);
    }

    @Test
    void listTest(){
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch("spring");
        questionQueryDTO.setPage(1);
        questionQueryDTO.setSize(5);
        questionQueryDTO.setUserId(5L);
        List<Question> questions = questionExtMapper.selectBySearch(questionQueryDTO);
        int i = questionExtMapper.countBySearch(questionQueryDTO);
        System.out.println(i);
        System.out.println(questions);
    }
}
