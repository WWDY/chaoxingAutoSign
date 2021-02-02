package com.daiju.cloudsign.domain;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daiju.cloudsign.entity.CourseInfo;
import com.daiju.cloudsign.entity.SignInfo;
import com.daiju.cloudsign.entity.User;
import com.daiju.cloudsign.entity.UserLoginInfo;
import com.daiju.cloudsign.mail.SendMail;
import com.daiju.cloudsign.mapper.CourseInfoMapper;
import com.daiju.cloudsign.mapper.UserLoginInfoMapper;
import com.daiju.cloudsign.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @Author WDY
 * @Date 2021-01-07 10:41
 * @Description TODO
 */
@Component
@Slf4j
public class SignService {
    @Autowired
    Sign sign;
    @Autowired
    GetCourseInfo getCourseInfo;

    @Autowired
    UserLoginInfoMapper userLoginInfoMapper;
    @Autowired
    CourseInfoMapper courseInfoMapper;
    @Autowired
    SendMail sendMail;

    @Autowired
    UserMapper userMapper;

    public boolean completeSignBefore() {
        ExecutorService threadPool = new ThreadPoolExecutor(
                5, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(), new CustomizableThreadFactory("Sign-Thread-pool-"));

        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            //查询有效的注册用户
            List<User> users = userMapper.selectList(new QueryWrapper<User>().ne("status", 0));
            //获取用户的超星账号
            List<String> collect = users.stream().map(User::getPhone).collect(Collectors.toList());
            //获取用户超星账号信息
            List<UserLoginInfo> userLoginInfos = userLoginInfoMapper.selectList(new QueryWrapper<UserLoginInfo>().in("phone", collect));
            return userLoginInfos;

        }, threadPool).thenAcceptAsync((userLoginInfos -> {
            //获取用户的所有课程并保存
            userLoginInfos.forEach(userLoginInfo -> {
                try {
                    getCourseInfo.saveCourseList(userLoginInfo.getCookies(), userLoginInfo.getPhone());
                } catch (Exception e) {
                    e.printStackTrace();
                    sendMail.sendExceptionMail(e.getMessage(), "707000180@qq.com");
                }
            });
        }), threadPool);
        try {
            future.get();
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            sendMail.sendExceptionMail(e.getMessage(), "707000180@qq.com");
            return false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            sendMail.sendExceptionMail(e.getMessage(), "707000180@qq.com");
            return false;
        }
    }

    public List<SignInfo> completeSignInfo(){
        //获取所有可用用户签到课程
        List<User> users = userMapper.selectList(new QueryWrapper<User>().ne("status", 0));
        List<SignInfo> signInfos = users.stream().map(user -> {
            QueryWrapper<CourseInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("phone", user.getPhone()).eq("status", 1);
            //用户所对应的所有的签到课程信息
            List<CourseInfo> courseInfos = courseInfoMapper.selectList(queryWrapper);
            UserLoginInfo userLoginInfo = userLoginInfoMapper.selectById(user.getPhone());
            SignInfo signInfo = new SignInfo();
            signInfo.setCourseInfos(courseInfos);
            signInfo.setCookies(userLoginInfo.getCookies());
            signInfo.setEmail(user.getEmail());
            return signInfo;
        }).collect(Collectors.toList());
        return signInfos;

    }
}
