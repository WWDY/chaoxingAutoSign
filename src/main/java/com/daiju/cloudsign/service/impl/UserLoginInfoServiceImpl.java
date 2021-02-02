package com.daiju.cloudsign.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daiju.cloudsign.entity.User;
import com.daiju.cloudsign.entity.UserLoginInfo;
import com.daiju.cloudsign.mapper.UserLoginInfoMapper;
import com.daiju.cloudsign.service.IUserLoginInfoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author WDY
 * @Date 2021-01-20 14:14
 * @Description
 */
@Service
public class UserLoginInfoServiceImpl implements IUserLoginInfoService {

    @Autowired
    UserLoginInfoMapper userLoginInfoMapper;


    @Override
    public UserLoginInfo findUserLoginInfoById(String id) {
        return userLoginInfoMapper.selectById(id);
    }

    @Override
    public List<UserLoginInfo> findUserLoginInfosByQueryWrapper(QueryWrapper<UserLoginInfo> queryWrapper) {
        return userLoginInfoMapper.selectList(queryWrapper);
    }

    @Override
    public int deleteUserLoginInfoById(String id) {
        return userLoginInfoMapper.deleteById(id);
    }

    @Override
    public int updateUserLoginInfoById(UserLoginInfo userLoginInfo) {
        return userLoginInfoMapper.updateById(userLoginInfo);
    }

    @Override
    public int addUserLoginInfo(UserLoginInfo userLoginInfo) {
        return userLoginInfoMapper.insert(userLoginInfo);
    }
}
