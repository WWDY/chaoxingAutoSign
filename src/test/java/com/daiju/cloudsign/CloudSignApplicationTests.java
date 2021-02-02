package com.daiju.cloudsign;


import com.daiju.cloudsign.domain.Sign;
import com.daiju.cloudsign.domain.SignService;
import com.daiju.cloudsign.domain.UserLogin;
import com.daiju.cloudsign.entity.CourseInfo;
import com.daiju.cloudsign.entity.SignInfo;
import com.daiju.cloudsign.entity.SignLocation;
import com.daiju.cloudsign.entity.User;
import com.daiju.cloudsign.mail.SendMail;
import com.daiju.cloudsign.mapper.CourseInfoMapper;
import com.daiju.cloudsign.mapper.UserLoginInfoMapper;
import com.daiju.cloudsign.util.HttpClientUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CloudSignApplicationTests {

    @Autowired
    UserLoginInfoMapper userLoginInfoMapper;

    @Autowired
    HttpClientUtils httpClientUtils;



    @Autowired
    SendMail sendMail;
    @Autowired
    UserLogin login;

    @Autowired
    CourseInfoMapper courseInfoMapper;

    @Autowired
    Sign sign;
    @Autowired
    SignService signService;
    @Test
    void te(){
       while (true) {
           if(signService.completeSignBefore()){
               List<SignInfo> signInfos = signService.completeSignInfo();
               signInfos.forEach(x->{
                   SignLocation signLocation = x.getSignLocation();
                   List<CourseInfo> courseInfos = x.getCourseInfos();
                   courseInfos.forEach(courseInfo->{
                       try {
                           sign.signin(courseInfo, x.getCookies(), x.getEmail(),signLocation);
                       } catch (Exception e) {
                           e.printStackTrace();
                       }
                   });
               });
               break;
           }

        }

    }

    @Test
    void test() throws Exception {


        //Document post = Jsoup.connect("http://api.902000.xyz:88/wkapi.php?q=马克思主义的最高社会理想是").timeout(10*1000).post();

        User user = new User();
        user.setEmail("111111111");
        System.out.println(user);


    }


}
