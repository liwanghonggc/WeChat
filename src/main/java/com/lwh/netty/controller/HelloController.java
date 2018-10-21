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
        logger.info("log test");
        logger.debug("log test");
        logger.error("log test");
        logger.trace("log test");
        logger.warn("log test");
        return "Hello WeChat";
    }
}
