package com.lwh.netty.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lwh
 * @date 2018-10-11
 * @desp 测试
 */

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "Hello WeChat";
    }
}
