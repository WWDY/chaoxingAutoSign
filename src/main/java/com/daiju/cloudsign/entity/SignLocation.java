package com.daiju.cloudsign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author WDY
 * @Date 2021-01-20 14:09
 * @Description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignLocation implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.INPUT)
    private String phone;
    private String address;
    private String longitude;
    private String latitude;
}
