package com.daiju.cloudsign.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author WDY
 * @Date 2021-01-07 14:51
 * @Description TODO
 */
@Data
public class SignInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String email;
    private String cookies;
    private SignLocation signLocation;
    private List<CourseInfo> courseInfos;
}
