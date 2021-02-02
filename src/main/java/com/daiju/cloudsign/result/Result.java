package com.daiju.cloudsign.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author WDY
 * @Date 2020-12-29 10:59
 * @Description TODO
 */
@Getter
@AllArgsConstructor
public enum Result {

    /**
     * 成功
     */
    SUCCESS(200,"success"),

    /**
     * 失败
     */
    FAIL(400,"error");


    private int code;
    private String msg;
}
