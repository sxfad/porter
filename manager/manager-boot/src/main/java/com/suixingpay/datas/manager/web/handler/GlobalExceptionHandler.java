package com.suixingpay.datas.manager.web.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suixingpay.datas.manager.exception.BaseException;
import com.suixingpay.datas.manager.exception.ExceptionCode;
import com.suixingpay.datas.manager.web.message.ResponseMessage;

/**
 * web层异常拦截
 * 
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseMessage inspector(Exception exception, HttpServletRequest request, HttpServletResponse response) {

        if (exception instanceof BaseException) {
            logger.info(exception.getMessage(), exception);
            BaseException e = (BaseException) exception;
            return ResponseMessage.error(e.getMessage(),
                    Integer.valueOf(e.getMessageCode() != null ? e.getMessageCode() : "0"));
        }
        logger.error(exception.getMessage(), exception);
        return ResponseMessage.error(exception.getMessage(), ExceptionCode.EXCEPTION_SYSTEM);

    }

}
