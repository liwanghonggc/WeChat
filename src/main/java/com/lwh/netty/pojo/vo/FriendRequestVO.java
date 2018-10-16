package com.lwh.netty.pojo.vo;

/**
 * @author lwh
 * @date 2018-10-16
 * @desp 接收用户请求的VO,好友请求发送方的信息
 */
public class FriendRequestVO {

    /**
     * 发送者ID
     */
    private String sendUserId;

    private String sendUsername;

    private String sendFaceImage;

    private String sendNickname;

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getSendUsername() {
        return sendUsername;
    }

    public void setSendUsername(String sendUsername) {
        this.sendUsername = sendUsername;
    }

    public String getSendFaceImage() {
        return sendFaceImage;
    }

    public void setSendFaceImage(String sendFaceImage) {
        this.sendFaceImage = sendFaceImage;
    }

    public String getSendNickname() {
        return sendNickname;
    }

    public void setSendNickname(String sendNickname) {
        this.sendNickname = sendNickname;
    }
}