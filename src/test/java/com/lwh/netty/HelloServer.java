package com.lwh.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author lwh
 * @date 2018-10-10
 * @desp 实现客户端发送一个请求,服务器返回hello netty
 */
public class HelloServer {

    public static void main(String[] args){

        //定义一对线程组
        //主线程组,用于接收客户端的连接,不做任何处理
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        //从线程组,老板线程组会把任务丢给它,让手写做任务
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //快速启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                           .childHandler(new HelloServerInitializer());

            //启动server,并且设置8088为启动的端口号,同时启动方式为同步
            ChannelFuture channelFuture = serverBootstrap.bind(8088).sync();

            //监听关闭的channel,设置为同步方式
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
