package com.joy.user.service;

import com.joy.thrift.user.UserInfo;
import com.joy.thrift.user.UserService;
import org.apache.thrift.TException;

public class UserServiceImpl implements UserService.Iface {

    @Override
    public UserInfo getUserById(int id) throws TException {

        return null;
    }

    @Override
    public UserInfo getUserByName(String username) throws TException {
        return null;
    }

    @Override
    public void registerUser(UserInfo userInfo) throws TException {

    }

}
