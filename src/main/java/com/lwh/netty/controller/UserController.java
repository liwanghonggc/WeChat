package com.lwh.netty.controller;

import com.lwh.netty.pojo.Users;
import com.lwh.netty.pojo.vo.UserVO;
import com.lwh.netty.service.UserService;
import com.lwh.netty.utils.MD5Utils;
import com.lwh.netty.utils.WeChatResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
