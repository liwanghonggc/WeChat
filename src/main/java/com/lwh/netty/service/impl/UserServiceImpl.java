package com.lwh.netty.service.impl;

import com.lwh.netty.enums.SearchFriendsStatusEnum;
import com.lwh.netty.mapper.MyFriendsMapper;
import com.lwh.netty.mapper.UsersMapper;
import com.lwh.netty.pojo.MyFriends;
import com.lwh.netty.pojo.Users;
import com.lwh.netty.service.UserService;
import com.lwh.netty.utils.FastDFSClient;
import com.lwh.netty.utils.FileUtils;
import com.lwh.netty.utils.QRCodeUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.io.IOException;

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

        MyFriends myFriendsRel = myFriendsMapper.selectOneByExample(criteria);
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

}
