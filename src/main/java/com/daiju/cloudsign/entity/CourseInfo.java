package com.daiju.cloudsign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author WDY
 * @Date 2021-01-05 23:54
 * @Description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String phone;
    private String name;
    private String courseid;
    private String clazzid;
    private String vc;
    private String cpi;
    private String status;
}
