package com.lwh.netty.websocket;

import io.netty.channel.Channel;

import java.util.HashMap;

/**
 * @author lwh
 * @date 2018-10-19
 * @desp 用户id和channel的关联关系
 */
public class UserChannelRel {

    private static HashMap<String, Channel> manager = new HashMap<>();

    public static void put(String senderId, Channel channel){
        manager.put(senderId, channel);
    }

    public static Channel get(String senderId){
        return manager.get(senderId);
    }

    /**
     * 测试方法
     */
    public static void output(){
        for(HashMap.Entry<String, Channel> entry : manager.entrySet()){
            System.out.println("UserId " + entry.getKey() + ", ChannelId: " + entry.getValue().id().asLongText());
        }
    }
}
