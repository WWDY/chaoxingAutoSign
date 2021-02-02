package com.daiju.cloudsign.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daiju.cloudsign.entity.ScheduledTask;
import com.baomidou.mybatisplus.extension.service.IService;
import com.daiju.cloudsign.entity.ScheduledTask;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author WDY
 * @since 2021-01-25
 */
public interface IScheduledTaskService{
    /**
     * 通过id查找课程定时任务信息
     * @param id    //id
     * @return ScheduledTask
     */
    @Cacheable(cacheNames = "scheduled_task",key = "#id" )
    ScheduledTask findScheduledTaskById(String id);

    /**
     * 通过条件查找课程定时任务信息
     * @param queryWrapper  //构造条件
     * @return List<ScheduledTask>
     */
    List<ScheduledTask> findScheduledTasksByQueryWrapper(QueryWrapper<ScheduledTask> queryWrapper);

    /**
     * 通过id删除课程定时任务信息
     * @param id    //id
     * @return int
     */
    @CacheEvict(cacheNames = "scheduled_task",key = "#id" )
    int deleteScheduledTaskById(String id);

    /**
     * 通过id更新课程定时任务信息
     * @param scheduledTask     //课程定时任务信息
     * @return int
     */
    @CacheEvict(cacheNames = "scheduled_task",key = "#scheduledTask.id" )
    int updateScheduledTaskById(ScheduledTask scheduledTask);
    /**
     * 添加课程定时任务信息
     * @param scheduledTask    //课程定时任务信息
     * @return
     */
    int addScheduledTask(ScheduledTask scheduledTask);

}
