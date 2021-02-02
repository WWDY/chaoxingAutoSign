package com.daiju.cloudsign.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daiju.cloudsign.entity.SignLocation;
import com.daiju.cloudsign.entity.User;
import com.daiju.cloudsign.entity.UserLoginInfo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @Author WDY
 * @Date 2021-01-20 14:12
 * @Description TODO
 */
public interface IUserLoginInfoService {
    /**
     * 通过id查找用户
     * @param id    //phone
     * @return UserLoginInfo
     */
    @Cacheable(cacheNames = "user_login_info",key = "#id")
    UserLoginInfo findUserLoginInfoById(String id);

    /**
     * 通过条件查找用户
     * @param queryWrapper  //构造条件
     * @return List<UserLoginInfo>
     */
    List<UserLoginInfo> findUserLoginInfosByQueryWrapper(QueryWrapper<UserLoginInfo> queryWrapper);

    /**
     * 通过id删除用户
     * @param id    //phone
     * @return int
     */
    @CacheEvict(cacheNames = "user_login_info",key = "#id")
    int deleteUserLoginInfoById(String id);

    /**
     * 通过id更新用户
     * @param userLoginInfo     //用户登录信息
     * @return int
     */
    @CacheEvict(cacheNames = "user_login_info",key = "#userLoginInfo.phone")
    int updateUserLoginInfoById(UserLoginInfo userLoginInfo);
    /**
     * 添加用户登录信息
     * @param userLoginInfo    //用户登录信息
     * @return
     */
    int addUserLoginInfo(UserLoginInfo userLoginInfo);
}
