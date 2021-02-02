package com.daiju.cloudsign.result;

/**
 * @Author WDY
 * @Date 2020-12-29 11:15
 * @Description TODO
 */
public class ResultFactory {
    public static ResultBody success(){
        return new ResultBody(Result.SUCCESS);
    }
    public static ResultBody success(String msg){
        return new ResultBody(Result.SUCCESS,msg);
    }
    public static ResultBody success(String msg,Object data){
        return new ResultBody(Result.SUCCESS,msg,data);
    }
    public static ResultBody success(Object data){
        return new ResultBody(Result.SUCCESS,data);
    }

    public static ResultBody fail(){
        return new ResultBody(Result.FAIL);
    }
    public static ResultBody fail(String msg){
        return new ResultBody(Result.FAIL,msg);
    }
    public static ResultBody fail(String msg,Object data){
        return new ResultBody(Result.FAIL,msg,data);
    }
    public static ResultBody fail(Object data) {
        return new ResultBody(Result.FAIL, data);
    }

    public static ResultBody resultBody(int code,String msg,Object data){
        return new ResultBody(code, msg, data);
    }
}
