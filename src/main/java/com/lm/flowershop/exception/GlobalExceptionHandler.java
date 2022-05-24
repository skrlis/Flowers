package com.lm.flowershop.exception;

import com.lm.flowershop.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author lm
 * @create 2022-03-22 11:08
 */

@ControllerAdvice
public class GlobalExceptionHandler  {

/*
  如果抛出的是ServiceException,则调用该方法
  GlobalExceptionHandler 全局异常处理器
  @param se 业务异常
  @return Result
*/
@ExceptionHandler(ServiceException.class)
@ResponseBody
    public Result handle(ServiceException se){
        return Result.error(se.getCode(),se.getMessage());

}
}
