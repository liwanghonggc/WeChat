package com.lwh.netty.controller;

import com.lwh.netty.enums.SearchFriendsStatusEnum;
import com.lwh.netty.pojo.Users;
import com.lwh.netty.pojo.bo.UserBO;
import com.lwh.netty.pojo.vo.UserVO;
import com.lwh.netty.service.UserService;
import com.lwh.netty.utils.FastDFSClient;
import com.lwh.netty.utils.FileUtils;
import com.lwh.netty.utils.MD5Utils;
import com.lwh.netty.utils.WeChatResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lwh
 * @date 2018-10-11
 * @desp 用户登录注册
 */

@RestController
@RequestMapping("u")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FastDFSClient fastDFSClient;

    @PostMapping("/registOrLogin")
    public WeChatResult registOrLogin(@RequestBody Users user) throws Exception{

        System.out.println(user.getUsername() + ", " + user.getPassword());

        //1.判断用户名和密码不能为空
        if(StringUtils.isBlank(user.getUsername())
                || StringUtils.isBlank(user.getPassword())){
            return WeChatResult.errorMsg("用户名或密码不能为空!");
        }

        //2.判断用户名是否存在,如果存在就登录,如果不存在就注册
        boolean userNameIsExist = userService.queryUserNameIsExist(user.getUsername());

        Users userResult = null;

        if(userNameIsExist){
            //2.1登录
            userResult = userService.queryUserForLogin(user.getUsername(), MD5Utils.getMD5Str(user.getPassword()));
            if(userResult == null){
                return WeChatResult.errorMsg("用户名或密码不正确!");
            }
        }else{
            //2.2注册
            user.setNickname(user.getUsername());
            user.setFaceImage("");
            user.setFaceImageBig("");

            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));

            userResult = userService.saveUser(user);
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userResult, userVO);

        return WeChatResult.ok(userVO);
    }

    /**
     * 图片上传,获取前端传过来的base64字符串,然后转换为文件对象再上传
     * @param userBO
     * @return
     * @throws Exception
     */
    @PostMapping("/uploadFaceBase64")
    public WeChatResult uploadFaceBase64(@RequestBody UserBO userBO) throws Exception {

        //获取前端传过来的base64字符串,然后转换为文件对象再上传
        String faceData = userBO.getFaceData();

        String userFacePath = "D:\\" + userBO.getUserId() + "userPic.png";
        FileUtils.base64ToFile(userFacePath, faceData);

        //上传文件到FastDFS
        MultipartFile faceFile = FileUtils.fileToMultipart(userFacePath);

        //这时候文件服务器里存了两份图片,一个是大图,一个是小图
        String imageUrl = fastDFSClient.uploadBase64(faceFile);

        System.out.println(imageUrl);

        //针对url切割,获取缩略图的URL
        String thump = "_80x80.";
        String[] arr = imageUrl.split("\\.");
        String thumpImgUrl = arr[0] + thump + arr[1];

        System.out.println(thumpImgUrl);

        Users user = new Users();
        user.setId(userBO.getUserId());
        user.setFaceImage(thumpImgUrl);
        user.setFaceImageBig(imageUrl);

        Users result = userService.updateUserInfo(user);

        //返回前端,前端进行刷新
        return WeChatResult.ok(result);
    }

    /**
     * 修改用户昵称
     * @param userBO
     * @return
     * @throws Exception
     */
    @PostMapping("/setNickname")
    public WeChatResult setNickname(@RequestBody UserBO userBO) throws Exception{
        Users user = new Users();

        user.setId(userBO.getUserId());
        user.setNickname(userBO.getNickname());

        Users result = userService.updateUserInfo(user);

        return WeChatResult.ok(result);
    }

    /**
     * 搜索添加好友
     * @return
     */
    @PostMapping("/search")
    public WeChatResult searchUser(@RequestBody UserBO userBO){
        String userId = userBO.getUserId();
        String friendUsername = userBO.getFriendUsername();
        System.out.println(userId + ", " + friendUsername);

        //1.判断是否为空
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(friendUsername)){
            return WeChatResult.errorMsg("字段为空");
        }

        //2.前置条件
        //2.1 搜索的用户如果不存在,返回无此用户
        //2.2 搜索账号是你自己，返回[不能添加自己]
        //2.3 搜索的朋友已经是你的好友，返回[该用户已经是你的好友]
        Integer status = userService.preconditionSearchFriends(userId, friendUsername);
        if(SearchFriendsStatusEnum.SUCCESS.status.equals(status)){
            Users user = userService.queryUserInfoByUsername(friendUsername);
            UserVO result = new UserVO();
            BeanUtils.copyProperties(user, result);
            return WeChatResult.ok(result);
        }else {
            String errorMsg = SearchFriendsStatusEnum.getMsgByKey(status);
            return WeChatResult.errorMsg(errorMsg);
        }

    }
}
