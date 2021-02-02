package com.daiju.cloudsign.quartz;

import com.daiju.cloudsign.domain.Sign;
import com.daiju.cloudsign.entity.CourseInfo;
import com.daiju.cloudsign.entity.SignLocation;
import com.daiju.cloudsign.entity.User;
import com.daiju.cloudsign.entity.UserLoginInfo;
import com.daiju.cloudsign.mail.SendMail;
import com.daiju.cloudsign.service.ICourseInfoService;
import com.daiju.cloudsign.service.ISignLocationService;
import com.daiju.cloudsign.service.IUserLoginInfoService;
import com.daiju.cloudsign.service.IUserService;
import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;
import javax.annotation.Resource;

/**
 * @Author WDY
 * @Date 2021-01-26 18:36
 * @Description TODO
 */
public class ScheduleJob extends QuartzJobBean {

    @Resource
    ISignLocationService signLocationService;

    @Resource
    ICourseInfoService courseInfoService;

    @Resource
    IUserLoginInfoService userLoginInfoService;

    @Resource
    IUserService userService;

    @Resource
    SendMail sendMail;

    @Resource
    Sign sign;


    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext){
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        String id = jobDetail.getJobDataMap().getString("id");
        String userId = jobDetail.getJobDataMap().getString("userId");
        CourseInfo courseInfo = courseInfoService.findCourseInfoById(id);
        String phone = courseInfo.getPhone();
        SignLocation signLocation = signLocationService.findSignLocationById(phone);
        UserLoginInfo userLoginInfo = userLoginInfoService.findUserLoginInfoById(phone);
        User user = userService.findUserById(userId);
        try {
            sign.signin(courseInfo, userLoginInfo.getCookies(), user.getEmail(), signLocation);
        } catch (Exception e) {
            sendMail.sendSignFailureMail(courseInfo, user.getEmail());
            sendMail.sendExceptionMail(e.getMessage(), "707000180@qq.com");
        }
    }
}
