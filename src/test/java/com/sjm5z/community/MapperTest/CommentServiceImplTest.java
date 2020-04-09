package com.sjm5z.community.MapperTest;

import com.sjm5z.community.dto.CommentDTO;
import com.sjm5z.community.dto.PaginationDTO;
import com.sjm5z.community.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommentServiceImplTest {

    @Autowired
    private CommentServiceImpl commentService;

    @Test
    void listByQuestionIdTest(){
        PaginationDTO<CommentDTO> commentDTOPaginationDTO = commentService.listByQuestionId((long) 1, 1,3 ,1);
//        System.out.println(commentDTOPaginationDTO);
        for (CommentDTO question : commentDTOPaginationDTO.getQuestions()) {
            System.out.println(question);
        }
    }
}
