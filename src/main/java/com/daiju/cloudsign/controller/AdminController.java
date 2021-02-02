package com.daiju.cloudsign.controller;

import cn.hutool.core.util.StrUtil;
import com.daiju.cloudsign.entity.User;
import com.daiju.cloudsign.result.ResultBody;
import com.daiju.cloudsign.result.ResultFactory;
import com.daiju.cloudsign.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author WDY
 * @Date 2021-01-31 14:01
 * @Description TODO
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    IUserService userService;
    @GetMapping("/users")
    public ResultBody getAllUser(@SessionAttribute("user") User user){
        if (user == null) {
            return ResultFactory.fail("当前未登录");
        }
        Integer status = user.getStatus();
        if(status == 1){
            return ResultFactory.fail("当前操作暂无权限");
        }else if(status == 2){
            List<User> users = userService.findUsersByQueryWrapper(null);
            List<User> userList = users.stream()
                    .peek(value-> value.setPassword(""))
                    .filter(val -> val.getStatus() == 1)
                    .collect(Collectors.toList());
            Map<String, Object> data = new HashMap<>(1);
            data.put("users", userList);
            return ResultFactory.success(data);
        }else{
            List<User> users = userService.findUsersByQueryWrapper(null);
            List<User> userList = users.stream()
                    .peek(value -> value.setPassword(""))
                    .filter(v->v.getStatus() != 3)
                    .collect(Collectors.toList());
            Map<String, Object> data = new HashMap<>(1);
            data.put("users", userList);
            return ResultFactory.success(data);
        }
    }

    @GetMapping("/status")
    public ResultBody getStatus(@SessionAttribute("user") User user) {
        if (user == null) {
            return ResultFactory.fail("当前未登录");
        }
        User userById = userService.findUserById(user.getUsername());
        return ResultFactory.success(String.valueOf(userById.getStatus()));
    }
    @DeleteMapping("/user")
    public ResultBody delUser(@SessionAttribute("user") User user,@RequestBody String id){
        if (user == null) {
            return ResultFactory.fail("当前未登录");
        }
        Integer status = user.getStatus();
        if(status == 1){
            return ResultFactory.fail("当前操作暂无权限");
        }
        if (userService.deleteUserById(id)>0) {
            return ResultFactory.success();
        }
        return ResultFactory.fail("删除失败");
    }
    @PutMapping("/user")
    public ResultBody updateUser(@SessionAttribute("user") User user,@Valid @RequestBody User newUser){
        if (user == null) {
            return ResultFactory.fail("当前未登录");
        }
        Integer status = user.getStatus();
        if(status == 1){
            return ResultFactory.fail("当前操作暂无权限");
        }
        if (StrUtil.isBlank(newUser.getPassword())) {
            newUser.setPassword(null);
        }
        if (userService.updateUserById(newUser)>0) {
            return ResultFactory.success();
        }

        return ResultFactory.fail("更新失败");
    }

}
