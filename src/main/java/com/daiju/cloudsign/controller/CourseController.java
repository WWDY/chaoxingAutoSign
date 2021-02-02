package com.daiju.cloudsign.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daiju.cloudsign.domain.GetCourseInfo;
import com.daiju.cloudsign.entity.*;
import com.daiju.cloudsign.mapper.QuestionMapper;
import com.daiju.cloudsign.result.ResultBody;
import com.daiju.cloudsign.result.ResultFactory;
import com.daiju.cloudsign.service.IScheduledTaskService;
import com.daiju.cloudsign.service.impl.CourseInfoServiceImpl;
import com.daiju.cloudsign.service.impl.UserLoginInfoServiceImpl;
import com.daiju.cloudsign.util.ScheduleTaskInfoUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

/**
 * @Author WDY
 * @Date 2021-01-21 17:22
 * @Description TODO
 */
@RequestMapping("/api/course")
@RestController
public class CourseController {
    @Resource
    CourseInfoServiceImpl courseInfoService;

    @Autowired
    GetCourseInfo getCourseInfo;

    @Autowired
    UserLoginInfoServiceImpl userLoginInfoService;

    @Autowired
    Scheduler scheduler;

    @Resource
    QuestionMapper questionMapper;

    @Autowired
    ScheduleTaskInfoUtils scheduleTaskInfoUtils;
    @Autowired
    IScheduledTaskService scheduledTaskService;

    @DeleteMapping("courseInfo/{id}")
    public ResultBody delCourse(@PathVariable("id") String id, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return ResultFactory.fail("当前未登录");
        }
        int res = courseInfoService.deleteCourseInfoById(id);
        if (res > 0) {
            return ResultFactory.success("删除成功");
        }
        return ResultFactory.fail("删除失败");
    }

    @GetMapping("/courseInfo")
    public ResultBody refreshCourse(HttpSession session) throws Exception {
        User user = (User)session.getAttribute("user");
        if (user == null) {
            return ResultFactory.fail("当前未登录");
        }
        String phone = user.getPhone();
        UserLoginInfo userLoginInfo = userLoginInfoService.findUserLoginInfoById(phone);
        getCourseInfo.saveCourseList(userLoginInfo.getCookies(),phone);
        List<CourseInfo> courseInfoList = courseInfoService.findCourseInfosByQueryWrapper(new QueryWrapper<CourseInfo>().eq("phone", phone));
        HashMap<String, Object> data = new HashMap<>(1);
        data.put("courseInfos", courseInfoList);
        return ResultFactory.success(data);
    }

    @PostMapping("/courseInfo/{id}/{status}")
    public ResultBody updataSignState(@PathVariable("id")String id,
                                      @PathVariable("status")String status,
                                      @RequestBody JSONObject jsonObject){
        JSONObject signInfos = jsonObject.getJSONObject("signInfos");
        CourseInfo courseInfo = signInfos.getJSONObject("courseInfo").toJavaObject(CourseInfo.class);
        courseInfo.setId(Integer.parseInt(id));
        courseInfo.setStatus(status);
        if(StrUtil.equals(status,"0")){
            if (scheduleTaskInfoUtils.delTask(courseInfo)) {
                courseInfoService.updateCourseInfoById(courseInfo);
                return ResultFactory.success();
            }
            return ResultFactory.fail();
        } else if (StrUtil.equals(status, "1")) {
            CronAndCalendar cronAndCalendar = null;
            try {
                cronAndCalendar = scheduleTaskInfoUtils.dealWithPostInfoConvertToCronExpression(signInfos);
            } catch (ParseException e) {
                return ResultFactory.fail();
            }
            ScheduledTask scheduledTask = scheduleTaskInfoUtils.getScheduledTask(cronAndCalendar.getScheduledTask(), courseInfo);
            String calendarName = courseInfo.getPhone() + courseInfo.getCourseid();
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
                        courseInfoService.updateCourseInfoById(courseInfo);
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
                courseInfoService.updateCourseInfoById(courseInfo);
                return ResultFactory.success();
            } catch (SchedulerException e) {
                e.printStackTrace();
                return ResultFactory.fail();
            }
        }

        return ResultFactory.fail();


    }

    @GetMapping("/search")
    public ResultBody searchQuestion(@RequestParam("q") String question){
        String url = "http://api.902000.xyz:88/wkapi.php?q="+question;
        JSONObject jsonObject;
        String answer;
        try {
            Question dbQuestion = questionMapper.selectById(question);
            if (dbQuestion == null) {
                Document post = Jsoup.connect(url).timeout(10*1000).post();
                String text = post.body().text();
                jsonObject = JSONObject.parseObject(text);
                answer = jsonObject.getString("answer");
                if(StrUtil.contains(answer,"暂未")){
                    answer = "该题目暂未收录";
                }else {
                    questionMapper.insert(new Question(question,answer));
                }
            }else {
                answer = dbQuestion.getAnswer();
            }
        } catch (IOException e) {
            return ResultFactory.success("查询失败，请稍后重试");
        }
        return ResultFactory.success(answer);
    }
}


