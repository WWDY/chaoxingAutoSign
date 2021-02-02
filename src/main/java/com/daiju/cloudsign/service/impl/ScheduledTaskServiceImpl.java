package com.daiju.cloudsign.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daiju.cloudsign.entity.ScheduledTask;
import com.daiju.cloudsign.mapper.ScheduledTaskMapper;
import com.daiju.cloudsign.service.IScheduledTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author WDY
 * @since 2021-01-25
 */
@Service
public class ScheduledTaskServiceImpl implements IScheduledTaskService {
    @Autowired
    ScheduledTaskMapper scheduledTaskMapper;


    @Override
    public ScheduledTask findScheduledTaskById(String id) {
        return scheduledTaskMapper.selectById(id);
    }

    @Override
    public List<ScheduledTask> findScheduledTasksByQueryWrapper(QueryWrapper<ScheduledTask> queryWrapper) {
        return scheduledTaskMapper.selectList(queryWrapper);
    }

    @Override
    public int deleteScheduledTaskById(String id) {
        return scheduledTaskMapper.deleteById(id);
    }

    @Override
    public int updateScheduledTaskById(ScheduledTask scheduledTask) {
        return scheduledTaskMapper.updateById(scheduledTask);
    }

    @Override
    public int addScheduledTask(ScheduledTask scheduledTask) {
        return scheduledTaskMapper.insert(scheduledTask);
    }
}
