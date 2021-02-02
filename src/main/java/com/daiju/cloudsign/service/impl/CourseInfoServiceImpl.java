package com.daiju.cloudsign.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daiju.cloudsign.entity.CourseInfo;
import com.daiju.cloudsign.mapper.CourseInfoMapper;
import com.daiju.cloudsign.service.ICourseInfoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author WDY
 * @Date 2021-01-20 14:14
 * @Description TODO
 */
@Service
public class CourseInfoServiceImpl implements ICourseInfoService {

    @Autowired
    CourseInfoMapper courseInfoMapper;

    @Override
    public CourseInfo findCourseInfoById(String id) {
        return courseInfoMapper.selectById(id);
    }

    @Override
    public List<CourseInfo> findCourseInfosByQueryWrapper(QueryWrapper<CourseInfo> queryWrapper) {
        return courseInfoMapper.selectList(queryWrapper);
    }

    @Override
    public int deleteCourseInfoById(String id) {
        return courseInfoMapper.deleteById(id);
    }

    @Override
    public int updateCourseInfoById(CourseInfo courseInfo) {
        return courseInfoMapper.updateById(courseInfo);
    }

    @Override
    public int addCourseInfo(CourseInfo courseInfo) {
        return courseInfoMapper.insert(courseInfo);
    }
}
