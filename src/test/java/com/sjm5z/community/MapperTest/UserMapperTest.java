package com.sjm5z.community.MapperTest;

import com.sjm5z.community.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void selectUserOfAccountIDTest(){
//        User user = userMapper.selectUserOfAccountID("59160292");
//        System.out.println(user);
    }

    @Test
    public void test(){
//        User user = userMapper.selectUserOfToken("e8344660-ff10-4678-92d3-526a7fd06c2d");
//        System.out.println(user);
//        userMapper.selectUserExist(1L);

    }
}
