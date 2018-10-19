package com.lwh.netty.service.impl;

import com.lwh.netty.enums.MsgSignFlagEnum;
import com.lwh.netty.enums.SearchFriendsStatusEnum;
import com.lwh.netty.mapper.*;
import com.lwh.netty.pojo.FriendsRequest;
import com.lwh.netty.pojo.MyFriends;
import com.lwh.netty.pojo.Users;
import com.lwh.netty.pojo.vo.FriendRequestVO;
import com.lwh.netty.pojo.vo.MyFriendsVO;
import com.lwh.netty.service.UserService;
import com.lwh.netty.utils.FastDFSClient;
import com.lwh.netty.utils.FileUtils;
import com.lwh.netty.utils.QRCodeUtils;
import com.lwh.netty.websocket.pojo.ChatMsg;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author lwh
 * @date 2018-10-11
 * @desp
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private QRCodeUtils qrCodeUtils;

    @Autowired
    private FastDFSClient fastDFSClient;

    @Autowired
    private MyFriendsMapper myFriendsMapper;

    @Autowired
    private FriendsRequestMapper friendsRequestMapper;

    @Autowired
    private UsersMapperCustom usersMapperCustom;

    @Autowired
    private ChatMsgMapper chatMsgMapper;

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public boolean queryUserNameIsExist(String userName) {

        Users user = new Users();
        user.setUsername(userName);

        Users result = usersMapper.selectOne(user);

        return result != null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public Users queryUserForLogin(String userName, String password) {

        Example userExample = new Example(Users.class);
        Criteria criteria = userExample.createCriteria();

        criteria.andEqualTo("username", userName);
        criteria.andEqualTo("password", password);

        Users result = usersMapper.selectOneByExample(userExample);
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public Users saveUser(Users user) {
        //生成唯一ID
        String userId = sid.nextShort();

        //为每一个用户生成二维码
        String qrCodePath = "D://user" + userId + "qrcode.png";
        //wechat_qrcode:[username]
        qrCodeUtils.createQRCode(qrCodePath, "wechat_qrcode:" + user.getUsername());
        MultipartFile qrCodeFile = FileUtils.fileToMultipart(qrCodePath);

        String qrCodeUrl = "";

        try {
            qrCodeUrl = fastDFSClient.uploadQRCode(qrCodeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        user.setQrcode(qrCodeUrl);
        user.setId(userId);
        usersMapper.insert(user);
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public Users updateUserInfo(Users user) {
        //Selective方法对于为空的字段不会更新,通常用这个
        usersMapper.updateByPrimaryKeySelective(user);
        return queryUserById(user.getId());
    }


    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public Users queryUserById(String userId){
        return usersMapper.selectByPrimaryKey(userId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public Integer preconditionSearchFriends(String userId, String friendName) {
        //1.搜索的用户不存在
        Users user = queryUserInfoByUsername(friendName);
        if(user == null){
            return SearchFriendsStatusEnum.USER_NOT_EXIST.status;
        }

        //2.搜索账号是你自己，返回[不能添加自己]
        if(user.getId().equals(userId)){
            return SearchFriendsStatusEnum.NOT_YOURSELF.status;
        }

        //3.搜索的朋友已经是你的好友，返回[该用户已经是你的好友]
        Example example = new Example(MyFriends.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("myUserId", userId);
        criteria.andEqualTo("myFriendUserId", user.getId());

        MyFriends myFriendsRel = myFriendsMapper.selectOneByExample(example);
        if(myFriendsRel != null){
            return SearchFriendsStatusEnum.ALREADY_FRIENDS.status;
        }

        return SearchFriendsStatusEnum.SUCCESS.status;
    }

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public Users queryUserInfoByUsername(String username){
        Example userExample = new Example(Users.class);

        Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username", username);

        return usersMapper.selectOneByExample(userExample);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void sendFriendRequest(String myUserId, String friendUsername) {
        //查询朋友的信息
        Users friend = queryUserInfoByUsername(friendUsername);

        //查询发送好友请求记录
        Example fre = new Example(FriendsRequest.class);
        Criteria frc = fre.createCriteria();
        frc.andEqualTo("sendUserId", myUserId);
        frc.andEqualTo("acceptUserId", friend.getId());

        FriendsRequest friendsRequest = friendsRequestMapper.selectOneByExample(fre);
        if(friendsRequest == null){
            //如果不是你的好友,并且好友记录没有添加,则新增好友请求记录
            String requestId = sid.nextShort();
            FriendsRequest request = new FriendsRequest();
            request.setId(requestId);
            request.setSendUserId(myUserId);
            request.setAcceptUserId(friend.getId());
            request.setRequestDateTime(new Date());
            friendsRequestMapper.insert(request);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public List<FriendRequestVO> queryFriendRequestList(String acceptUserId) {
        return usersMapperCustom.queryFriendRequestList(acceptUserId);
    }

    /**
     * 删除好友请求记录
     * @param sendUseId
     * @param acceptUserId
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void deleteFriendRequest(String sendUseId, String acceptUserId) {
        Example fre = new Example(FriendsRequest.class);
        Criteria frc = fre.createCriteria();
        frc.andEqualTo("sendUserId", sendUseId);
        frc.andEqualTo("acceptUserId", acceptUserId);
        friendsRequestMapper.deleteByExample(fre);
    }

    /**
     * 通过好友请求
     * @param sendUseId
     * @param acceptUserId
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void passFriendRequest(String sendUseId, String acceptUserId) {
        //1.保存好友记录
        saveFriends(sendUseId, acceptUserId);
        //2.逆向保存好友记录
        saveFriends(acceptUserId, sendUseId);
        //3.删除好友请求记录
        deleteFriendRequest(sendUseId, acceptUserId);
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    void saveFriends(String sendUserId, String acceptUserId){
        MyFriends myFriends = new MyFriends();
        String recordId = sid.nextShort();
        myFriends.setId(recordId);
        myFriends.setMyFriendUserId(acceptUserId);
        myFriends.setMyUserId(sendUserId);
        myFriendsMapper.insert(myFriends);
    }

    /**
     * 搜索显示好友通讯录
     * @param userId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public List<MyFriendsVO> queryMyFriends(String userId) {
        List<MyFriendsVO> myFriendsVOList = usersMapperCustom.queryMyFriends(userId);
        return myFriendsVOList;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public String saveMsg(ChatMsg chatMsg) {
        com.lwh.netty.pojo.ChatMsg msgDB = new com.lwh.netty.pojo.ChatMsg();

        String msgId = sid.nextShort();
        msgDB.setId(msgId);
        msgDB.setAcceptUserId(chatMsg.getReceiveId());
        msgDB.setSendUserId(chatMsg.getSenderId());
        msgDB.setCreateTime(new Date());
        msgDB.setSignFlag(MsgSignFlagEnum.unsign.type);
        msgDB.setMsg(chatMsg.getMsg());

        chatMsgMapper.insert(msgDB);
        return msgId;
    }

}
