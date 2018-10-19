package com.lwh.netty.websocket.pojo;

import java.io.Serializable;

/**
 * @author lwh
 * @date 2018-10-19
 * @desp 聊天消息
 */
public class ChatMsg implements Serializable {

    private static final long serialVersionUID = -3496409340321298036L;

    /**
     * 发送者ID
     */
    private String senderId;

    /**
     * 接收者ID
     */
    private String receiveId;

    /**
     * 聊天内容
     */
    private String msg;

    /**
     * 消息ID,用于签收
     */
    private String msgId;


    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
