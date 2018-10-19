package com.lwh.netty.websocket.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author lwh
 * @date 2018-10-19
 * @desp 用于检测channel的心跳handler
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        //判断evt是否是IdleStateEvent,用于触发用户事件,包含读空闲、写空闲、读写空闲
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;

            if(event.state() == IdleState.READER_IDLE){
                //不需要处理
                System.out.println("进入读空闲");
            }else if(event.state() == IdleState.WRITER_IDLE){
                //不需要处理
                System.out.println("进入写空闲");
            }else if(event.state() == IdleState.ALL_IDLE){
                //说明客户端可能开启了类似飞行模式
                System.out.println("进入读写空闲");
                Channel channel = ctx.channel();
                channel.close();
                System.out.println(ChatHandler.users.size());
            }
        }
    }
}
