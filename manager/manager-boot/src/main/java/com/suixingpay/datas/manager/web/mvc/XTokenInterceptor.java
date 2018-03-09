/**
 * 
 */
package com.suixingpay.datas.manager.web.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.suixingpay.datas.manager.web.token.TokenUtil;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
@Component
public class XTokenInterceptor extends HandlerInterceptorAdapter {

    private Logger log = LoggerFactory.getLogger(XTokenInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String uri = request.getRequestURI();
        log.info("requestURI:{}", uri);
        try {
            String handlerMethod = request.getMethod();
            if ("OPTIONS".equals(handlerMethod)) {
                return true;
            }
            if (!(handler instanceof HandlerMethod)) {
                return true;
            }
            // 校验token
            String token = request.getHeader("X-Token");
            log.info("传入的token值:{}", token);
            if (StringUtils.isEmpty(token) || !TokenUtil.isValid(token)) {
                log.info("token校验错误");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().append(JSON.toJSONString(new AuthorizedBody("401", "token Check the error")));
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    class AuthorizedBody {

        AuthorizedBody(String code, String name) {
            this.code = code;
            this.name = name;
        }

        private String code;
        private String name;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
