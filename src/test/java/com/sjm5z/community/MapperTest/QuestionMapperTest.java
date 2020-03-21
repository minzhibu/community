package com.sjm5z.community.MapperTest;

import com.sjm5z.community.mapper.QuestionMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class QuestionMapperTest {

    @Autowired
    private QuestionMapper questionMapper;

    @Test
    public void findAllAndUserTest() {
//        List<QuestionDTO> allAndUser = questionMapper.findAllAndUser(offset, size);
//        System.out.println(allAndUser);
    }
}
