package com.hegoo.disgo.utils;

import com.alibaba.fastjson2.JSON;

import java.io.Serializable;

/**
 * 统一响应结果封装
 */
public class Result<T> implements Serializable {
    private int code;
    private String message;
    private T data;

    public Result(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public int getCode() {
        return code;
    }


    public Result<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }


    public Result<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    //不带数据的成功结果
    public static Result SUCCESS_NO_DATA() {
        return new Result(ResultCode.SUCCESS);
    }

    //带数据的成功结果
    public static <T> Result<T> SUCCESS_WITH_DATA(T data) {
        Result result = new Result(ResultCode.SUCCESS);
        result.setData(data);
        return result;
    }

    //不带数据的失败结果
    public static Result FAIL_NO_DATA() {
        return new Result(ResultCode.CONNECTION_ERROR);
    }

    //带数据的失败结果
    public static <T> Result<T> FAIL_WITH_DATA(T data) {
        Result result = new Result(ResultCode.CONNECTION_ERROR);
        result.setData(data);
        return result;
    }
}
