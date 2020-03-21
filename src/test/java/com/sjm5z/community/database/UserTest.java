package com.sjm5z.community.database;

import com.sjm5z.community.mapper.UserMapper;
import com.sjm5z.community.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void InsertTest() {
        System.out.println(userMapper);
        User user = new User();
        user.setName("1");
        user.setGmtModified(1L);
        user.setGmtCreate(1L);
        user.setToken("1");
        System.out.println(user);
        userMapper.insert(user);
    }

}
