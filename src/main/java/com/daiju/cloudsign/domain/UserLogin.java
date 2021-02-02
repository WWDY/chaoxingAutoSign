package com.daiju.cloudsign.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.daiju.cloudsign.entity.UserLoginInfo;
import com.daiju.cloudsign.mapper.UserLoginInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

/**
 * @Author WDY
 * @Date 2021-01-04 23:57
 * @Description TODO
 */
@Component
@Slf4j
public class UserLogin {

    @Autowired
    UserLoginInfoMapper userLoginInfoMapper;

    public boolean loginByPhoneAndPassword(String phone,String password) throws IOException {
        BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        String url = "http://passport2.chaoxing.com/fanyalogin";
        HttpPost httpPost = new HttpPost(url);
        String encode = Base64.getEncoder().encodeToString(password.getBytes());
        httpPost.setHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        httpPost.setHeader("Host","passport2.chaoxing.com");
        httpPost.setHeader("X-Requested-With","XMLHttpRequest");
        System.out.println(encode);
        String text = "fid=-1&uname="+phone+"&password="+ encode +"&refer=http%253A%252F%252Fi.chaoxing.com&t=true&forbidotherlogin=0";
        StringEntity stringEntity = new StringEntity(text);
        stringEntity.setContentType("application/x-www-form-urlencoded");
        stringEntity.setContentEncoding("UTF-8");
        httpPost.setEntity(stringEntity);
        CloseableHttpResponse execute = httpClient.execute(httpPost);
        String s = EntityUtils.toString(execute.getEntity());
        JSONObject resultJson = JSON.parseObject(s);
        log.info("\n用户：{}\n登录信息：{}",phone,resultJson.toString());
        if (resultJson.getString("status")!=null&&resultJson.getString("status").equals("true")) {
            List<Cookie> cookies = cookieStore.getCookies();
            StringBuilder cookieStringBuilder = new StringBuilder();
            cookies.forEach(x -> {
                cookieStringBuilder.append(x.getName()+"="+x.getValue()+";");
            });

            log.info("\ncookie信息:{}",cookieStringBuilder);
            UserLoginInfo userLoginInfo = new UserLoginInfo();
            userLoginInfo.setPhone(phone);
            userLoginInfo.setPassword(password);
            userLoginInfo.setCookies(cookieStringBuilder.toString());
            if (userLoginInfoMapper.selectById(phone) != null) {
                userLoginInfoMapper.updateById(userLoginInfo);
                return true;
            } else {
                userLoginInfoMapper.insert(userLoginInfo);
                return true;
            }

        }
        return false;
    }
}
