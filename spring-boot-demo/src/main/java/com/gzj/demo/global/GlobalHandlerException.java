package com.gzj.demo.global;

import com.gzj.demo.common.BizException;
import com.gzj.demo.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalHandlerException {


    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error(e.getMessage());
        return Result.ERROR("系统发生未知错误请联系管理员");
    }

    @ExceptionHandler(BizException.class)
    public Result<?> handlerBizException(BizException e) {
        log.error(e.getMsg());
        return Result.ERROR(e.getCode(), e.getMsg());
    }

}
