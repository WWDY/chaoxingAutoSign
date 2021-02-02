package com.daiju.cloudsign.domain;

import cn.hutool.core.util.ReUtil;
import com.daiju.cloudsign.entity.CourseInfo;
import com.daiju.cloudsign.entity.SignLocation;
import com.daiju.cloudsign.mail.SendMail;
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

import java.util.*;

/**
 * @Author WDY
 * @Date 2021-01-06 16:57
 * @Description 签到处理
 */
@Component
@Slf4j
public class Sign {

    @Autowired
    HttpClientUtils httpClientUtils;

    @Autowired
    SendMail sendMail;

    /**
     * 获取签到任务
     *
     * @param courseInfo    //签到课程信息
     */
    public Map<String, String> getSignTask(CourseInfo courseInfo, String cookies) throws Exception {
        String url = "https://mobilelearn.chaoxing.com/widget/pcpick/stu/index?courseId=" + courseInfo.getCourseid() + "&jclassId=" + courseInfo.getClazzid();
        Map<String, String> headers = new HashMap<>(2);
        headers.put("Host", "mobilelearn.chaoxing.com");
        headers.put("Cookie", cookies);
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClientUtils.get(httpGet, headers);
        HttpEntity res = response.getEntity();
        String s = EntityUtils.toString(res);
        Document parse = Jsoup.parse(s);
        String signTask = parse.select("#1").text();
        List<String> flag = ReUtil.findAll("[0-9]", signTask, 0);
        Elements select = parse.select(".Mct");
        Elements signName = parse.select(".Mct>div>a");

        Map<String, String> activeids = new HashMap<>(20);
        if (Integer.parseInt(flag.get(0)) > 0) {
            for (int i = 0; i < Integer.parseInt(flag.get(0)); i++) {
                String attribute = select.get(i).attr("onclick");
                Integer number = ReUtil.getFirstNumber(attribute);
                activeids.put(String.valueOf(number), signName.get(i).text());
            }
            activeids.forEach((k, v) -> System.out.println(k + "===" + v));
            return activeids;
        }
        return new HashMap<>(1);
    }


    /**
     *
     * @param courseInfo    //课程信息
     * @param cookies   //用户cookie
     * @param email     //通知邮箱
     * @param signLocation  //签到信息
     * @return //签到结果
     * @throws Exception    //签到发生异常
     */
    public boolean signin(CourseInfo courseInfo, String cookies, String email, SignLocation signLocation) throws Exception {

        Map<String, String> signTask = this.getSignTask(courseInfo, cookies);
        Iterator<Map.Entry<String, String>> iterator = signTask.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            String k = next.getKey();
            String v = next.getValue();
            String url;
            if (("位置签到").equals(v)) {
                url = "https://mobilelearn.chaoxing.com/pptSign/stuSignajax?activeId=" + k + "&clientip=&latitude=" + signLocation.getLatitude() + "&longitude=" + signLocation.getLongitude() + "&appType=15&fid=0&address=" + signLocation.getAddress();
            } else {
                url = "https://mobilelearn.chaoxing.com/pptSign/stuSignajax?activeId=" + k + "&clientip=&appType=15&fid=0";
            }
            this.executeSign(url,cookies,iterator,courseInfo,email);
        }

        return true;
    }

    public void executeSign(String url,String cookies,Iterator iterator,CourseInfo courseInfo,String email){
        HttpGet httpGet = new HttpGet(url);
        HashMap<String, String> headers = new HashMap<>(1);
        headers.put("Cookie", cookies);
        try {
            CloseableHttpResponse response = httpClientUtils.get(httpGet, headers);
            String res = EntityUtils.toString(response.getEntity());
            if ("您已签到过了".equals(res)) {
                iterator.remove();
            } else if ("success".equals(res)) {
                iterator.remove();
                log.info("\n用户：{}签到成功", courseInfo.getPhone());
                sendMail.sendSignSuccessMail(courseInfo, email);
            }
        } catch (Exception e) {
            log.info("\n用户：{}签到失败", courseInfo.getPhone());
            sendMail.sendSignFailureMail(courseInfo, email);
            e.printStackTrace();
        }
    }

}
