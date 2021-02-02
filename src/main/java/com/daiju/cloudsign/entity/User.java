package com.daiju.cloudsign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import java.io.Serializable;

/**
 * @Author WDY
 * @Date 2021-01-07 14:41
 * @Description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.INPUT)
    private String username;
    private String password;
    @Email
    private String email;
    private String phone;
    private Integer status;

}
