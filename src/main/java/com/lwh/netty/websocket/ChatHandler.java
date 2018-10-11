package com.lwh.netty.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

/**
 * @author lwh
 * @date 2018-10-10
 * @desp 处理消息的Handler,TextWebSocketFrame是用于WebSocket处理文本的对象,frame是消息的载体
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     * 用于记录和管理所有的客户端channel
     */
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //获取客户端传过来的消息
        String content = msg.text();

        System.out.println("接收到的数据: " + content);

        for(Channel channel : clients){
            channel.writeAndFlush(
                    new TextWebSocketFrame("[服务器在: ]" + LocalDateTime.now() + " 接收到消息, 消息为: " + content));
        }
    }

    /**
     * 当客户端连接服务端之后
     * 获取客户端的channel,并且放到ChannelGroup中去进行管理
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        clients.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //当触发handlerRemoved,ChannelGroup会自动移除客户端的channel
        //clients.remove(ctx.channel());

        System.out.println("客户端断开,channel对应的长id为: " + ctx.channel().id().asLongText());
        System.out.println("客户端断开,channel对应的短id为: " + ctx.channel().id().asShortText());
    }
}
