package com.daiju.cloudsign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author WDY
 * @Date 2021-01-05 9:56
 * @Description TODO
 */
@Data
public class UserLoginInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.INPUT)
    private String phone;
    private String password;
    private String cookies;
    private String userName;
    private String school;
}
