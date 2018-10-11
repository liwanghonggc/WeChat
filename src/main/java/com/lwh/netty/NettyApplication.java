package com.lwh.netty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.lwh.netty.mapper")
@ComponentScan(basePackages = {"com.lwh.netty", "org.n3r.idworker"})

/**
 * 扫描MyBatis的mapper路径
 */
public class NettyApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyApplication.class, args);
    }
}
