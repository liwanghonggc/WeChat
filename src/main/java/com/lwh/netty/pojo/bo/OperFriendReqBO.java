package com.lwh.netty.pojo.bo;

/**
 * @author lwh
 * @date 2018-10-17
 * @desp 接收前台对于好友请求的处理结果
 */
public class OperFriendReqBO {

    private String acceptUserId;

    private String sendUserId;

    private Integer operType;

    public String getAcceptUserId() {
        return acceptUserId;
    }

    public void setAcceptUserId(String acceptUserId) {
        this.acceptUserId = acceptUserId;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public Integer getOperType() {
        return operType;
    }

    public void setOperType(Integer operType) {
        this.operType = operType;
    }
}
