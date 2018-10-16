package com.lwh.netty.mapper;


import com.lwh.netty.pojo.Users;
import com.lwh.netty.pojo.vo.FriendRequestVO;
import com.lwh.netty.utils.MyMapper;

import java.util.List;

/**
 * 一些关联查询的SQL
 */
public interface UsersMapperCustom extends MyMapper<Users> {

    /**
     * 搜索添加好友的信息
     * @param acceptUserId
     * @return
     */
    List<FriendRequestVO> queryFriendRequestList(String acceptUserId);
}