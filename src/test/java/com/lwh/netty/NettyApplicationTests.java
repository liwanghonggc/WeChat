package com.lwh.netty;

import com.lwh.netty.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NettyApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    public void contextLoads() {
        String userId = "181014FM6KGR4000";
        String username = "xcj";
        Integer res = userService.preconditionSearchFriends(userId, username);
    }

}
