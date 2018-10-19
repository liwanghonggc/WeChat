package com.lwh.netty.websocket.pojo;

import java.io.Serializable;

/**
 * @author lwh
 * @date 2018-10-19
 * @desp
 */
public class DataContent implements Serializable {

    /**
     * Alt insert 快捷键生成序列化ID
     */
    private static final long serialVersionUID = -3696588780469986703L;

    /**
     * 动作类型
     */
    private Integer action;

    /**
     * 用户的聊天内容
     */
    private ChatMsg chatMsg;

    /**
     * 额外扩展参数
     */
    private String extend;

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public ChatMsg getChatMsg() {
        return chatMsg;
    }

    public void setChatMsg(ChatMsg chatMsg) {
        this.chatMsg = chatMsg;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
}
