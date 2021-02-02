package com.daiju.cloudsign.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author WDY
 * @Date 2020-12-29 11:01
 * @Description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultBody implements Serializable {
    private static final Long serialVersionUID = 1L;
    private int code;
    private String msg;
    private Object data;

    public ResultBody(Result result){
        this.code = result.getCode();
        this.msg = result.getMsg();
    }
    public ResultBody(Result result,Object data){
        this.code = result.getCode();
        this.msg = result.getMsg();
        this.data = data;
    }
    public ResultBody(Result result,String msg){
        this.code = result.getCode();
        this.msg = msg;
    }
    public ResultBody(Result result,String msg,Object data){
        this.code = result.getCode();
        this.msg = msg;
        this.data = data;
    }
}
