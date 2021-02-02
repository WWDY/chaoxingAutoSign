package com.daiju.cloudsign.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daiju.cloudsign.entity.CourseInfo;
import com.daiju.cloudsign.entity.SignLocation;
import com.daiju.cloudsign.entity.UserLoginInfo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @Author WDY
 * @Date 2021-01-20 14:39
 * @Description TODO
 */
public interface ISignLocationService {
    /**
     * 通过id查找用户签到信息
     * @param id    //id
     * @return User
     */
    @Cacheable(cacheNames = "sign_location",key = "#id" )
    SignLocation findSignLocationById(String id);

    /**
     * 通过条件查找用户签到信息
     * @param queryWrapper  //构造条件
     * @return List<User>
     */
    List<SignLocation> findSignLocationsByQueryWrapper(QueryWrapper<SignLocation> queryWrapper);

    /**
     * 通过id删除用户签到信息
     * @param id    //id
     * @return int
     */
    @CacheEvict(cacheNames = "sign_location",key = "#id")
    int deleteSignLocationById(String id);

    /**
     * 通过id更新用户签到信息
     * @param signLocation     //用户签到信息
     * @return int
     */
    @CacheEvict(cacheNames = "sign_location",key = "#signLocation.phone")
    int updateSignLocationById(SignLocation signLocation);
    /**
     * 添加用户签到信息
     * @param signLocation    //用户签到信息
     * @return
     */
    int addSignLocation(SignLocation signLocation);
}
