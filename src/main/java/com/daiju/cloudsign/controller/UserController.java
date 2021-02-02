package com.daiju.cloudsign.controller;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daiju.cloudsign.domain.UserLogin;
import com.daiju.cloudsign.entity.CourseInfo;
import com.daiju.cloudsign.entity.SignLocation;
import com.daiju.cloudsign.entity.User;
import com.daiju.cloudsign.entity.UserLoginInfo;
import com.daiju.cloudsign.result.ResultBody;
import com.daiju.cloudsign.result.ResultFactory;
import com.daiju.cloudsign.service.impl.CourseInfoServiceImpl;
import com.daiju.cloudsign.service.impl.SignLocationServiceImpl;
import com.daiju.cloudsign.service.impl.UserLoginInfoServiceImpl;
import com.daiju.cloudsign.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author WDY
 * @Date 2021-01-19 17:38
 * @Description TODO
 */
@RestController
@RequestMapping("/api/user/")
public class UserController {

    @Autowired
    UserLogin userLogin;

    @Autowired
    UserLoginInfoServiceImpl userLoginInfoService;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    SignLocationServiceImpl signLocationService;
    @Autowired
    CourseInfoServiceImpl courseInfoService;

    @GetMapping("/userInfo")
    public ResultBody getUserInfo(HttpSession session){
        User user = (User)session.getAttribute("user");
        if (user == null) {
            return ResultFactory.fail("当前未登录");
        }
        String username = user.getUsername();
        String phone = userService.findUserById(username).getPhone();
        if (StrUtil.isEmpty(phone)){
            return ResultFactory.fail("暂未绑定超星帐号");
        }
        QueryWrapper<CourseInfo> courseInfoQueryWrapper = new QueryWrapper<>();
        courseInfoQueryWrapper.eq("phone",phone);
        List<CourseInfo> courseInfos = courseInfoService.findCourseInfosByQueryWrapper(courseInfoQueryWrapper);
        String email = userService.findUsersByQueryWrapper(new QueryWrapper<User>().eq("phone",phone)).get(0).getEmail();
        UserLoginInfo userLoginInfo = userLoginInfoService.findUserLoginInfoById(phone);
        SignLocation signLocation = signLocationService.findSignLocationById(phone);
        JSONObject data = new JSONObject();
        data.put("phone",userLoginInfo.getPhone());
        data.put("userName",userLoginInfo.getUserName());
        data.put("schoolName",userLoginInfo.getSchool());
        data.put("email",email);
        data.put("location",signLocation.getAddress());
        data.put("longitude",signLocation.getLongitude());
        data.put("latitude",signLocation.getLatitude());
        data.put("courseInfos",courseInfos);
        return ResultFactory.success(data);
    }


    @PutMapping("/signLocation")
    public ResultBody updateSignLocation(@RequestBody SignLocation signInfo){
        if (signInfo != null) {
            int res = signLocationService.updateSignLocationById(signInfo);
            if(res > 0){
                return ResultFactory.success("保存成功");
            }
        }
        return ResultFactory.fail("保存失败");
    }

    @PostMapping("/signLocation")
    public ResultBody addSignLocation(@RequestBody SignLocation signInfo){
        if (signInfo != null) {
            int res = signLocationService.updateSignLocationById(signInfo);
            if(res > 0){
                return ResultFactory.success("保存成功");
            }
        }
        return ResultFactory.fail("保存失败");
    }


    @PostMapping("/bindAccount")
    public ResultBody bindAccount(HttpSession session,@RequestBody JSONObject jsonObject) throws IOException {
        User user = (User)session.getAttribute("user");
        if (user == null) {
            return ResultFactory.fail("当前未登录");
        }
        String username = user.getUsername();
        String phone = jsonObject.getString("phone");
        String password = jsonObject.getString("password");
        String email = jsonObject.getString("email");
        if(!ReUtil.isMatch("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$",email)){
            return ResultFactory.fail("请输入正确的邮箱地址");
        }
        boolean res = userLogin.loginByPhoneAndPassword(phone, password);
        if(res){
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPhone(phone);
            newUser.setEmail(email);
            userService.updateUserById(newUser);
            return ResultFactory.success("绑定超星帐号成功");
        }
        return ResultFactory.fail("绑定超星帐号或密码错误");
    }


    @PutMapping("/email")
    public ResultBody changeEmail(@RequestBody JSONObject jsonObject,HttpSession session){
        User user = (User)session.getAttribute("user");
        if (user == null) {
            return ResultFactory.fail("当前未登录");
        }
        String username = user.getUsername();
        String email = jsonObject.getString("email");
        if(!ReUtil.isMatch("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$",email)){
            return ResultFactory.fail("请输入正确的邮箱地址");
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        int res = userService.updateUserById(newUser);
        if (res > 0) {
            return ResultFactory.success();
        }
        return ResultFactory.fail("保存失败");
    }

    @PostMapping("/phone")
    public ResultBody changePhone(@RequestBody JSONObject jsonObject, HttpSession session) throws IOException {
        User user = (User)session.getAttribute("user");
        if (user == null) {
            return ResultFactory.fail("当前未登录");
        }
        String phone = jsonObject.getString("phone");
        String password = jsonObject.getString("password");
        boolean res = userLogin.loginByPhoneAndPassword(phone, password);
        if (res) {
            String username = user.getUsername();
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPhone(phone);
            int resp = userService.updateUserById(newUser);
            if (resp > 0) {
                return ResultFactory.success();
            }
            return ResultFactory.fail("换绑失败");
        }
        return ResultFactory.fail("换绑失败");
    }

    @GetMapping("/baseUser")
    public ResultBody getUser(HttpSession session){
        User user = (User)session.getAttribute("user");
        if (user == null) {
            return ResultFactory.fail("当前未登录");
        }
        String username = user.getUsername();
        User userById = userService.findUserById(username);
        UserLoginInfo userLoginInfoById = userLoginInfoService.findUserLoginInfoById(userById.getPhone());
        Map<String, Object> data = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("account",username);
        jsonObject.put("phone",userById.getPhone());
        jsonObject.put("email",userById.getEmail());
        jsonObject.put("name",userLoginInfoById.getUserName());
        jsonObject.put("schoolName",userLoginInfoById.getSchool());
        data.put("user",jsonObject);
        return ResultFactory.success(data);
    }

    @PostMapping("/password")
    public ResultBody changePassword(@RequestBody JSONObject jsonObject,@SessionAttribute("user") User user){
        if(user == null){
            return ResultFactory.fail("当前未登录");
        }
        String oldPass = jsonObject.getString("oldPass");
        String newPass = jsonObject.getString("newPass");
        String username = user.getUsername();
        String password = userService.findUserById(username).getPassword();
        if(StrUtil.equals(password,oldPass)){
            User newUser = new User();
            newUser.setPassword(newPass);
            newUser.setUsername(username);
            if (userService.updateUserById(newUser)>0) {
                return ResultFactory.success("修改成功");
            }
            return ResultFactory.fail("修改失败");
        }
        return ResultFactory.fail("原密码不正确");
    }
}
