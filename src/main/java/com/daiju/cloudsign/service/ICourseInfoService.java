package com.daiju.cloudsign.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daiju.cloudsign.entity.CourseInfo;
import com.daiju.cloudsign.entity.UserLoginInfo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @Author WDY
 * @Date 2021-01-20 14:12
 * @Description TODO
 */
public interface ICourseInfoService {
    /**
     * 通过id查找课程信息
     * @param id    //phone
     * @return CourseInfo
     */
    @Cacheable(cacheNames = "course_info",key = "#id" )
    CourseInfo findCourseInfoById(String id);

    /**
     * 通过条件查找课程信息
     * @param queryWrapper  //构造条件
     * @return List<CourseInfo>
     */
    List<CourseInfo> findCourseInfosByQueryWrapper(QueryWrapper<CourseInfo> queryWrapper);

    /**
     * 通过id删除课程信息
     * @param id    //phone
     * @return int
     */
    @CacheEvict(cacheNames = "course_info",key = "#id" )
    int deleteCourseInfoById(String id);

    /**
     * 通过id更新课程信息
     * @param courseInfo     //课程信息信息
     * @return int
     */
    @CacheEvict(cacheNames = "course_info",key = "#courseInfo.id" )
    int updateCourseInfoById(CourseInfo courseInfo);

    /**
     * 添加课程信息
     * @param courseInfo    //课程信息
     * @return
     */
    int addCourseInfo(CourseInfo courseInfo);
}
