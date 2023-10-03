package com.free.forum;

import com.free.forum.mapper.MessageMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ForumApplicationTests {

    @Autowired
    private MessageMapper messageMapper;

    @Test
    void contextLoads() {

    }

}
