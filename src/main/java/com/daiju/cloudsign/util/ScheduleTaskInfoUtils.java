package com.daiju.cloudsign.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.daiju.cloudsign.entity.CourseInfo;
import com.daiju.cloudsign.entity.CronAndCalendar;
import com.daiju.cloudsign.entity.ScheduledTask;
import com.daiju.cloudsign.entity.User;
import com.daiju.cloudsign.quartz.ScheduleJob;
import org.quartz.*;
import org.quartz.impl.calendar.CronCalendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.text.ParseException;

/**
 * @Author WDY
 * @Date 2021-01-26 18:39
 * @Description TODO
 */
@Component
public class ScheduleTaskInfoUtils {
    @Autowired
    Scheduler scheduler;
    @Autowired
    HttpSession session;
    public CronAndCalendar dealWithPostInfoConvertToCronExpression(JSONObject signInfos) throws ParseException {
        String frequency = signInfos.getString("frequency");
        String time = StrUtil.subBetween(signInfos.getString("time"), "[", "]").replaceAll(" ","");
        String week = StrUtil.subBetween(signInfos.getString("week"), "[", "]").replaceAll(" ","");
        String[] splitTime = StrUtil.split(time, ",");
        String startTime = splitTime[0];
        String startTimeHour = StrUtil.split(startTime,":")[0];
        String startTimeMinute = StrUtil.split(startTime,":")[1];;
        String endTime = splitTime[1];
        String endTimeHour = StrUtil.split(endTime,":")[0];
        String endTimeMinute = StrUtil.split(endTime,":")[1];
        ScheduledTask scheduledTask = new ScheduledTask();
        scheduledTask.setStarTime(startTime);
        scheduledTask.setEndTime(endTime);
        int resEndTimeHour;
        if(Integer.parseInt(endTimeHour) + 1 == 24){
            resEndTimeHour = 0;
        }else {
            resEndTimeHour = Integer.parseInt(endTimeHour) + 1;
        }
        String cronExp;
        CronCalendar cronCalendar =  new CronCalendar("0 "+endTimeMinute+" "+endTimeHour+"-"+resEndTimeHour+" ? * "+week);
        switch (frequency){
            case "1":
                cronExp = "* */1 "+startTimeHour+"-"+resEndTimeHour+" ? * "+week;
                break;
            case "5":
                cronExp = "* */5 "+startTimeHour+"-"+resEndTimeHour+" ? * "+week;
                break;
            case "10":
                cronExp = "* */10 "+startTimeHour+"-"+resEndTimeHour+" ? * "+week;
                break;
            case "30":
                cronExp = "*/30 * "+startTimeHour+"-"+resEndTimeHour+" ? * "+week;
                break;
            default:
                cronExp = "* */5 8-19 ? * 2-6";
                cronCalendar = new CronCalendar("0 30 18-19 ? * 2-6");
                break;
        }
        return new CronAndCalendar(cronExp, cronCalendar,scheduledTask);
    }

    public boolean addCalendar(Calendar calendar,String calendarName){
        try {
            scheduler.addCalendar(calendarName,calendar,false,false);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 添加job和trigger到scheduler
     */
    public void addJobAndTrigger(CronAndCalendar cronAndCalendar,CourseInfo courseInfo) throws SchedulerException {
        ScheduledTask scheduledTask = cronAndCalendar.getScheduledTask();
        String groupId = scheduledTask.getTaskGroup();
        String taskId = scheduledTask.getTaskId();
        JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class)
                .withIdentity(taskId,groupId)
                .usingJobData("id",StrUtil.toString(courseInfo.getId()))
                .usingJobData("userId",((User)session.getAttribute("user")).getUsername())
                .build();
        CronTrigger jobTrigger = TriggerBuilder.newTrigger()
                .withIdentity(taskId, groupId)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronAndCalendar.getCron()))
                .modifiedByCalendar(scheduledTask.getId())
                .startNow()
                .build();
        try {
            scheduler.scheduleJob(jobDetail, jobTrigger);
        } catch (SchedulerException e) {
            scheduler.pauseTrigger(jobTrigger.getKey());
            scheduler.unscheduleJob(jobTrigger.getKey());
            scheduler.deleteJob(jobDetail.getKey());
            this.addJobAndTrigger(cronAndCalendar,courseInfo);
        }
    }

    public ScheduledTask getScheduledTask(ScheduledTask scheduledTask,CourseInfo courseInfo){
        scheduledTask.setTaskId(courseInfo.getCourseid());
        scheduledTask.setTaskGroup(courseInfo.getPhone());
        scheduledTask.setTaskDesc(courseInfo.getName());
        scheduledTask.setId(courseInfo.getPhone()+courseInfo.getCourseid());
        return scheduledTask;
    }

    public boolean delCalendar(CourseInfo courseInfo,String calendarName){
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(courseInfo.getCourseid(),courseInfo.getPhone()));
            scheduler.unscheduleJob(TriggerKey.triggerKey(courseInfo.getCourseid(),courseInfo.getPhone()));
            scheduler.deleteJob(JobKey.jobKey(courseInfo.getCourseid(),courseInfo.getPhone()));
            return scheduler.deleteCalendar(calendarName);
        } catch (SchedulerException e) {
            return false;
        }
    }

    public boolean delTask(CourseInfo courseInfo){
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(courseInfo.getCourseid(),courseInfo.getPhone()));
            scheduler.unscheduleJob(TriggerKey.triggerKey(courseInfo.getCourseid(),courseInfo.getPhone()));
            scheduler.deleteJob(JobKey.jobKey(courseInfo.getCourseid(),courseInfo.getPhone()));
            return true;
        } catch (SchedulerException e) {
            return false;
        }
    }
}
