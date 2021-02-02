package com.daiju.cloudsign.domain;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daiju.cloudsign.entity.CourseInfo;
import com.daiju.cloudsign.entity.UserLoginInfo;
import com.daiju.cloudsign.mapper.CourseInfoMapper;
import com.daiju.cloudsign.mapper.UserLoginInfoMapper;
import com.daiju.cloudsign.util.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author WDY
 * @Date 2021-01-05 22:58
 * @Description 获取课程信息
 */
@Component
@Slf4j
public class GetCourseInfo {
    @Autowired
    HttpClientUtils httpClientUtils;

    @Autowired
    UserLoginInfoMapper userLoginInfoMapper;

    @Autowired
    CourseInfoMapper courseInfoMapper;

    @Autowired
    UserLogin userLogin;


    /**
     * 获取对应帐号的所有课程信息
     * @param cookies   //
     * @param phone //
     * @throws Exception    //
     */
    public void saveCourseList(String cookies,String phone) throws Exception {
        String url = "http://mooc1-1.chaoxing.com/visit/interaction";
        Map<String, String> headers = new HashMap<>(2);
        headers.put("Host","mooc1-1.chaoxing.com");
        headers.put("Cookie",cookies);
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClientUtils.get(httpGet, headers);
        InputStream content = response.getEntity().getContent();
        Document parse = Jsoup.parse(content, "UTF-8", url);
        Elements ul = parse.select("h3.clearfix>a");
        int x=0;
        if (ul==null || ul.toString().length() <= 0) {
            UserLoginInfo userLoginInfo = userLoginInfoMapper.selectById(phone);
            userLogin.loginByPhoneAndPassword(phone, userLoginInfo.getPassword());
            if(x < 3){
                saveCourseList(userLoginInfoMapper.selectById(phone).getCookies(),phone);
                x++;
            }else {
                log.info("\n用户：{}登录失败",phone);
            }

        }else{
            Map<String,String> courseMetaDate = new HashMap<>(10);
            ul.forEach(li->{
                String href = li.attr("href");
                String name = li.attr("title");
                courseMetaDate.put(href.split("\\?")[1], name);
            });
            //查询数据库中该用户现有的数据
            QueryWrapper<CourseInfo> courseInfoQueryWrapper = new QueryWrapper<>();
            courseInfoQueryWrapper.eq("phone",phone);
            List<CourseInfo> currentCourseInfos = courseInfoMapper.selectList(courseInfoQueryWrapper);
            Set<String> courseIds = currentCourseInfos.stream().map(CourseInfo::getCourseid).collect(Collectors.toSet());

            List<CourseInfo> courseInfos = new ArrayList<>();
            courseMetaDate.forEach((k,v)->{
                CourseInfo courseInfo = new CourseInfo();
                courseInfo.setPhone(phone);
                courseInfo.setName(v);
                courseInfo.setStatus("0");
                String[] split = k.split("&");
                for (String s : split) {
                    if(s.startsWith("courseid")){
                        courseInfo.setCourseid(s.split("=")[1]);
                    }else if(s.startsWith("clazzid")){
                        courseInfo.setClazzid(s.split("=")[1]);
                    }else if(s.startsWith("vc")){
                        courseInfo.setVc(s.split("=")[1]);
                    } else if (s.startsWith("cpi")) {
                        courseInfo.setCpi(s.split("=")[1]);
                    }
                }
                if(!courseIds.contains(courseInfo.getCourseid())){
                    courseInfos.add(courseInfo);
                }
            });

            if (courseInfos.size() > 0) {
                courseInfoMapper.insertBatchSomeColumn(courseInfos);
            }

        }


    }

    public void getUserInfo(String cookies,String phone) throws Exception {
        String url = "http://i.chaoxing.com/base";
        HttpGet httpGet = new HttpGet(url);
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie",cookies);
        CloseableHttpResponse response = httpClientUtils.get(httpGet, headers);
        HttpEntity entity = response.getEntity();
        String res = EntityUtils.toString(entity);
        Document parse = Jsoup.parse(res);
        String schoolName = parse.select("p#siteName").attr("title");
        String userName = parse.select("p.user-name").text();
        int x = 0;
        if (userName.length() > 1 && schoolName.length() > 1) {
            UserLoginInfo userLoginInfo = new UserLoginInfo();
            userLoginInfo.setUserName(userName);
            userLoginInfo.setPhone(phone);
            userLoginInfo.setSchool(schoolName);
            userLoginInfoMapper.updateById(userLoginInfo);
        }else {
            if (x < 3) {
                this.getUserInfo(cookies,phone);
                x++;
            }
            return;

        }

    }




}
