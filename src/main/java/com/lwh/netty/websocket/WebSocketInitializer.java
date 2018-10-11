package com.lwh.netty.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author lwh
 * @date 2018-10-10
 * @desp
 */
public class WebSocketInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //WebSocket基于HTTP协议,所以需要HTTP编解码器
        pipeline.addLast(new HttpServerCodec());

        //对写大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());

        //对httpMessage进行聚合,聚合成FullHttpRequest或者FullHttpResponse
        //几乎在netty的编程中,都会用到该handler
        pipeline.addLast(new HttpObjectAggregator(1024 * 64));

        //以上三个是用于支持HTTP协议

        //用于WebSocket服务器处理的协议,用于指定客户端连接访问的路由
        //此handler会帮你处理一些繁重复杂的事,会帮你处理握手动作,handshaking
        //对于WebSocket来说,都是以frames来进行传输的,不同的数据类型对应的frame也不同
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));


        //添加用户自定义的handler
        pipeline.addLast(new ChatHandler());
    }
}
