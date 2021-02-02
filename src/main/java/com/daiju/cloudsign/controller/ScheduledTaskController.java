package com.daiju.cloudsign.controller;


import com.alibaba.fastjson.JSONObject;
import com.daiju.cloudsign.entity.CourseInfo;
import com.daiju.cloudsign.entity.CronAndCalendar;
import com.daiju.cloudsign.entity.ScheduledTask;
import com.daiju.cloudsign.result.ResultBody;
import com.daiju.cloudsign.result.ResultFactory;
import com.daiju.cloudsign.service.ICourseInfoService;
import com.daiju.cloudsign.service.IScheduledTaskService;
import com.daiju.cloudsign.util.ScheduleTaskInfoUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;

/**
 * @author WDY
 * @since 2021-01-25
 */
@RestController
@RequestMapping("/api/scheduledTask")
public class ScheduledTaskController {

    @Resource
    ScheduleTaskInfoUtils scheduleTaskInfoUtils;

    @Autowired
    IScheduledTaskService scheduledTaskService;

    @Autowired
    ICourseInfoService courseInfoService;



    @PostMapping("/signTask")
    public ResultBody addScheduleTask(@RequestBody JSONObject jsonObject) throws ParseException {
        JSONObject signInfos = jsonObject.getJSONObject("signInfos");
        CourseInfo courseInfo = signInfos.getJSONObject("courseInfo").toJavaObject(CourseInfo.class);
        courseInfo.setStatus("1");
        courseInfoService.updateCourseInfoById(courseInfo);
        CronAndCalendar cronAndCalendar = scheduleTaskInfoUtils.dealWithPostInfoConvertToCronExpression(signInfos);
        ScheduledTask scheduledTask = scheduleTaskInfoUtils.getScheduledTask(cronAndCalendar.getScheduledTask(),courseInfo);
        String calendarName = courseInfo.getPhone()+courseInfo.getCourseid();
        boolean res = scheduleTaskInfoUtils.addCalendar(cronAndCalendar.getCalendar(), calendarName);
        ScheduledTask task = scheduledTaskService.findScheduledTaskById(courseInfo.getPhone() + courseInfo.getCourseid());
        if(!res){
            if(scheduleTaskInfoUtils.delCalendar(courseInfo, calendarName)&&scheduleTaskInfoUtils.addCalendar(cronAndCalendar.getCalendar(), calendarName)){
                try {
                    scheduleTaskInfoUtils.addJobAndTrigger(cronAndCalendar,courseInfo);
                    if (task == null) {
                        scheduledTaskService.addScheduledTask(scheduledTask);
                    } else {
                        scheduledTaskService.updateScheduledTaskById(scheduledTask);
                    }
                    return ResultFactory.success();
                } catch (SchedulerException e) {
                    e.printStackTrace();
                    return ResultFactory.fail();
                }
            }
        }
        try {
            scheduleTaskInfoUtils.addJobAndTrigger(cronAndCalendar,courseInfo);
            if (task == null) {
                scheduledTaskService.addScheduledTask(scheduledTask);
            } else {
                scheduledTaskService.updateScheduledTaskById(scheduledTask);
            }
            return ResultFactory.success();
        } catch (SchedulerException e) {
            e.printStackTrace();
            return ResultFactory.fail();
        }

    }
}

