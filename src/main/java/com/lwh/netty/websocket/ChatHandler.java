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
    private static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //1.获取客户端传过来的消息
        String content = msg.text();

        //2.判断消息的类型,根据不同的类型处理不同的业务
        //2.1 当WebSocket第一次open时,初始化channel时,把用户的channel和UserId关联起来
        //2.2 聊天类型的消息,把聊天记录保存到数据库,同时标记消息的签收状态(未签收)
        //2.3 签收消息类型,针对具体的消息进行签收,修改数据库中对应的消息的签收状态(已签收)
        //2.4 心跳类型的消息
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
        users.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //当触发handlerRemoved,ChannelGroup会自动移除客户端的channel
        //这里我们手动remove掉
        users.remove(ctx.channel());
    }

    /**
     * 出现异常
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        //发生异常之后关闭channel,随后从ChannelGroup中移除
        ctx.channel().close();
        users.remove(ctx.channel());
    }
}
