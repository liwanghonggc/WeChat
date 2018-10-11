package com.lwh.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;

/**
 * @author lwh
 * @date 2018-10-10
 * @desp 修改启动方式,由SpringBoot启动
 */

@Component
public class WebSocketServer {

    private static class SingletonWebSocketServer{
        static final WebSocketServer instance = new WebSocketServer();
    }

    public static WebSocketServer getInstance(){
        return SingletonWebSocketServer.instance;
    }


    private EventLoopGroup parentGroup;

    private EventLoopGroup childGroup;

    private ServerBootstrap serverBootstrap;

    private ChannelFuture channelFuture;

    public WebSocketServer(){
        parentGroup = new NioEventLoopGroup();
        childGroup = new NioEventLoopGroup();
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(parentGroup, childGroup).channel(NioServerSocketChannel.class)
                .childHandler(new WebSocketInitializer());
    }

    public void start(){
        this.channelFuture = serverBootstrap.bind(8088);
        System.err.println("Netty WebSocket server 启动完毕!");
    }


}
