package com.gzj.demo.common;

import lombok.Data;

@Data
public class BizException extends RuntimeException{
    private Integer code;
    private String msg;
    public BizException(ResultCode r) {
        this.code = r.getCode();
        this.msg = r.getMsg();
    }
    public BizException(Integer code,String msg) {
        this.code = code;
        this.msg = msg;
    }
}
