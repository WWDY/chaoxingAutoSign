package com.daiju.cloudsign.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daiju.cloudsign.entity.User;
import com.daiju.cloudsign.mapper.UserMapper;
import com.daiju.cloudsign.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author WDY
 * @Date 2021-01-20 14:13
 * @Description
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User findUserById(String id) {
        return userMapper.selectById(id);
    }

    @Override
    public List<User> findUsersByQueryWrapper(QueryWrapper<User> queryWrapper) {
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public int deleteUserById(String id) {
       return userMapper.deleteById(id);

    }

    @Override
    public int updateUserById(User user) {
        return userMapper.updateById(user);
    }

    @Override
    public int addUser(User user) {
        return userMapper.insert(user);
    }
}
