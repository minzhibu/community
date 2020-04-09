package com.sjm5z.community.MapperTest;

import com.sjm5z.community.advice.CustomizeExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
public class CustomizeTest {
    @Autowired
    CustomizeExceptionHandler customizeExceptionHandler;


}
