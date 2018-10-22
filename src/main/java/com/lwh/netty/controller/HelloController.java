package com.lwh.netty.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lwh
 * @date 2018-10-11
 * @desp 测试
 */

@RestController
public class HelloController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/hello")
    public String hello(){
        String str = "hello.test";
        String[] splits = str.split("\\.");
        return splits[0] + splits[1];
    }
}
