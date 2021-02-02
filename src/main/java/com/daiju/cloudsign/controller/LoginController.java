package com.daiju.cloudsign.controller;

import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.util.StrUtil;
import com.daiju.cloudsign.entity.User;
import com.daiju.cloudsign.result.ResultBody;
import com.daiju.cloudsign.result.ResultFactory;
import com.daiju.cloudsign.service.impl.UserServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @Author WDY
 * @Date 2021-01-07 22:06
 * @Description TODO
 */
@RestController
@RequestMapping("/api/user")
public class LoginController {

    @Resource
    UserServiceImpl userService;

    @PostMapping("/login")
    public ResultBody login(@RequestParam("username")String username, @RequestParam("password")String password,
                            HttpSession session,@RequestParam("code")String code){
        if (!StrUtil.equals(code,(String)session.getAttribute("code"))) {
            return ResultFactory.fail("请输入正确的验证码");
        }else if(StrUtil.isEmpty(username) || StrUtil.isEmpty(password)){
            return ResultFactory.fail("账号密码不能为空");
        }else {
            User user = userService.findUserById(username);
            if (user == null) {
                return ResultFactory.fail("账号不存在");
            }else {
                if (StrUtil.equals(password, user.getPassword())&&StrUtil.equalsIgnoreCase(code,(String)session.getAttribute("code"))) {
                    session.setAttribute("user",user);
                    return ResultFactory.success("登录成功");
                }else{
                    return ResultFactory.fail("账号或密码不正确");
                }
            }
        }
    }

    @GetMapping("/logout")
    public ResultBody logout(HttpSession session){
        session.removeAttribute("user");
        return ResultFactory.success("注销登录成功");
    }

    @PostMapping("/register")
    public ResultBody register(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam("code")String code,HttpSession session) {
        System.out.println(username+"==="+password+"==="+code);
        if(!String.valueOf(session.getAttribute("code")).equals(code)){
            return ResultFactory.fail("验证码错误");
        }
        if (StrUtil.isEmpty(username) || StrUtil.isEmpty(password)) {
            return ResultFactory.fail("账号密码不能为空");
        }else if(userService.findUserById(username)==null){
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setStatus(1);
            userService.addUser(user);
            return ResultFactory.success("注册成功");
        }else {
            return ResultFactory.fail("帐号已存在");
        }
    }

    @GetMapping("/code")
    public void registerCode(HttpSession session, HttpServletResponse response) throws IOException {
        //定义图形验证码的长、宽、验证码字符数、干扰线宽度
        ShearCaptcha captcha = new ShearCaptcha(250, 100, 4, 4);
        //图形验证码写出，可以写出到文件，也可以写出到流
        captcha.write(response.getOutputStream());
        //验证图形验证码的有效性，返回boolean值
        session.setAttribute("code",captcha.getCode());
    }



}
