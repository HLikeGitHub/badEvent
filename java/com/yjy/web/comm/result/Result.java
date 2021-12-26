package com.yjy.web.comm.result;

/**
 * HTTP返回对象封装
 * @info Result
 * @author rwx
 * @email aba121mail@qq.com
 */
public class Result<T> {
    /**
     * 结果编码
     */
    private Integer code;
    /**
     * 结果信息
     */
    private String msg;
    /**
     * 具体数据
     */
    private T data;

    public Result() {}

    public Result(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public Result(ResultCode code){
        this.code = code.getCode();
        this.msg = code.getMsg();
    }

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(ResultCode code, T data){
        this.code = code.getCode();
        this.msg = code.getMsg();
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}