package com.lwh.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author lwh
 * @date 2018-10-10
 * @desp 初始化器,channel注册之后,会执行里面响应的初始化方法
 */
public class HelloServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();

        //HttpServerCodec是由Netty自己提供的助手类,可以理解为拦截器
        //当请求来到服务端,我们需要做解码,响应到客户端编码
        pipeline.addLast("httpServerCodec", new HttpServerCodec());

        //添加自定义的助手类,返回Hello Netty
        pipeline.addLast("customHandler", new CustomHandler());
    }
}
