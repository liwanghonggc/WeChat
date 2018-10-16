package com.lwh.netty.service;

import com.lwh.netty.pojo.Users;

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
}
