package com.sjm5z.community.MapperTest;

import com.sjm5z.community.mapper.UserMapper;
import com.sjm5z.community.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void selectUserOfAccountIDTest(){
        User user = userMapper.selectUserOfAccountID("59160292");
        System.out.println(user);
    }

    @Test
    public void test(){
        System.out.println((int)'1');
        System.out.println((int)'9');
    }
}
