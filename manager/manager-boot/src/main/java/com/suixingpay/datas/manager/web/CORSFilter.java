package com.suixingpay.datas.manager.web;

import org.springframework.core.annotation.Order;

import javax.servlet.FilterChain;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月06日 11:11
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月06日 11:11
 */
@Order(1)
@WebFilter(filterName = "CorsFilter", urlPatterns = "/*")
public class CORSFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse responseHttp = (HttpServletResponse) response;
        responseHttp.setHeader("Access-Control-Allow-Origin", "*");
        responseHttp.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE");
        responseHttp.setHeader("Access-Control-Max-Age", "3600");
        responseHttp.setHeader("Access-Control-Allow-Headers",
                "Content-Type, Accept, X-Requested-With, remember-me, X-Token");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
