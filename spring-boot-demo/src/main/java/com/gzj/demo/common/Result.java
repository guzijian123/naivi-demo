package com.gzj.demo.common;

import lombok.Data;

import static com.gzj.demo.common.ResultCode.CODE_200;
import static com.gzj.demo.common.ResultCode.CODE_500;


/**
 * 返回结果
 * @author 20412
 */
@Data
public class Result<T extends Object> {

    private Boolean success;

    private Integer code;
    private String message;

    private T data ;

    private Result() {}

    /** 成功静态方法 */
    public static Result OK() {
        Result r = new Result();
        r.setSuccess(true);
        r.setCode(CODE_200.getCode());
        r.setMessage("成功");
        return r;
    }

    /** 成功静态方法 */
    public static <T> Result<T> OK(T data) {
        Result r = new Result();
        r.setSuccess(true);
        r.setCode(CODE_200.getCode());
        r.setData(data);
        r.setMessage("成功");
        return r;
    }

    /** 成功静态方法 */
    public static <T> Result<T> OK(T data, String message) {
        Result r = new Result();
        r.setSuccess(true);
        r.setCode(CODE_200.getCode());
        r.setData(data);
        r.setMessage(message);
        return r;
    }

    /** 失败静态方法 */
    public static Result ERROR() {
        Result r = new Result();
        r.setSuccess(false);
        r.setCode(CODE_500.getCode());
        r.setMessage("失败");
        return r;
    }

    /** 失败静态方法 */
    public static Result ERROR(String message) {
        Result r = new Result();
        r.setSuccess(false);
        r.setCode(CODE_500.getCode());
        r.setMessage(message);
        return r;
    }

    /** 失败静态方法 */
    public static Result ERROR(Integer code,String message) {
        Result r = new Result();
        r.setSuccess(false);
        r.setCode(code);
        r.setMessage(message);
        return r;
    }
    /** 设置状态码 */
    public Result code(Integer code){
        this.setCode(code);
        return this;
    }

    /** 设置集合 */
    public Result<T> data(T map){
        this.setData(map);
        return this;
    }
}
