package com.lwh.netty.service;

import com.lwh.netty.pojo.Users;
import com.lwh.netty.pojo.vo.FriendRequestVO;
import com.lwh.netty.pojo.vo.MyFriendsVO;
import com.lwh.netty.websocket.pojo.ChatMsg;

import java.util.List;

/**
 * @author lwh
 * @date 2018-10-11
 * @desp
 */
public interface UserService {

    /**
     * 判断用户名是否存在
     * @param userName 用户名
     * @return
     */
    boolean queryUserNameIsExist(String userName);

    /**
     * 查询用户是否存在
     * @param userName 用户名
     * @param password 密码,要MD5加密
     * @return
     */
    Users queryUserForLogin(String userName, String password);

    /**
     * 用户注册
     * @param user
     * @return
     */
    Users saveUser(Users user);

    /**
     * 修改用户信息
     * @param user
     */
    Users updateUserInfo(Users user);

    /**
     * 搜索朋友的前置条件
     * @param userId 用户ID
     * @param friendName 要添加的好友用户名
     * @return
     */
    Integer preconditionSearchFriends(String userId, String friendName);

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    Users queryUserInfoByUsername(String username);

    /**
     * 发送好友添加请求,保存该记录到数据库
     * @param myUserId
     * @param friendUsername
     */
    void sendFriendRequest(String myUserId, String friendUsername);

    /**
     * 搜索添加好友的信息
     * @param acceptUserId
     * @return
     */
    List<FriendRequestVO> queryFriendRequestList(String acceptUserId);

    /**
     * 删除好友请求记录
     * @param sendUseId
     * @param acceptUserId
     */
    void deleteFriendRequest(String sendUseId, String acceptUserId);

    /**
     * 通过好友请求
     * @param sendUseId
     * @param acceptUserId
     */
    void passFriendRequest(String sendUseId, String acceptUserId);

    /**
     * 查询好友列表
     * @param userId
     * @return
     */
    List<MyFriendsVO> queryMyFriends(String userId);

    /**
     * 保存聊天信息,返回值为消息ID,根据它做签收
     * @param chatMsg
     * @return
     */
    String saveMsg(ChatMsg chatMsg);

    /**
     * 批量签收消息
     * @param msgIdList
     */
    void updateMsgSigned(List<String> msgIdList);
}
