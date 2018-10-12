package com.lwh.netty.service.impl;

import com.lwh.netty.mapper.UsersMapper;
import com.lwh.netty.pojo.Users;
import com.lwh.netty.service.UserService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

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

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUserNameIsExist(String userName) {

        Users user = new Users();
        user.setUsername(userName);

        Users result = usersMapper.selectOne(user);

        return result != null;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String userName, String password) {

        Example userExample = new Example(Users.class);
        Criteria criteria = userExample.createCriteria();

        criteria.andEqualTo("username", userName);
        criteria.andEqualTo("password", password);

        Users result = usersMapper.selectOneByExample(userExample);
        return result;
    }

    @Override
    public Users saveUser(Users user) {
        //生成唯一ID
        String userId = sid.nextShort();

        //TODO, 为每一个用户生成二维码
        user.setQrcode("");

        user.setId(userId);

        usersMapper.insert(user);
        return user;
    }
}
