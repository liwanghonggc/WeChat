package com.lwh.netty;

import com.lwh.netty.pojo.Users;
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
        Users user = userService.queryUserInfoByUsername("xcj");
        System.out.println(user.getUsername());
    }

}
