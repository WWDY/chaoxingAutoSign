package com.daiju.cloudsign.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daiju.cloudsign.entity.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @Author WDY
 * @Date 2021-01-20 14:12
 * @Description TODO
 */
public interface IUserService {
    /**
     * 通过id查找用户
     * @param id    //phone
     * @return User
     */
    @Cacheable(cacheNames = "user",key = "#id")
    User findUserById(String id);

    /**
     * 通过条件查找用户
     * @param queryWrapper  //构造条件
     * @return List<User>
     */
    List<User> findUsersByQueryWrapper(QueryWrapper<User> queryWrapper);

    /**
     * 通过id删除用户
     * @param id    //phone
     * @return int
     */
    @CacheEvict(cacheNames = "user",key = "#id")
    int deleteUserById(String id);

    /**
     * 通过id更新用户
     * @param user  //用户信息
     * @return int
     */
    @CacheEvict(cacheNames = "user",key = "#user.username")
    int updateUserById(User user);
    /**
     * 添加用户信息
     * @param user    //用户信息
     * @return int
     */
    int addUser(User user);

}
