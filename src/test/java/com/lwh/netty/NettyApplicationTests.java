package com.lwh.netty;

import com.lwh.netty.service.UserService;
import com.lwh.netty.utils.FastDFSClient;
import com.lwh.netty.utils.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NettyApplicationTests {

    @Autowired
    private FastDFSClient fastDFSClient;

    @Test
    public void contextLoads() throws Exception{

    }

}
