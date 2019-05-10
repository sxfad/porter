/**
 * Copyright ©2018 vbill.cn.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package cn.vbill.middleware.porter.manager.web.mvc;

import cn.vbill.middleware.porter.manager.web.rcc.RoleCheckContext;
import cn.vbill.middleware.porter.manager.web.tl.WebToeknContext;
import com.alibaba.fastjson.JSON;
import cn.vbill.middleware.porter.manager.web.token.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
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
            String requestMethod = request.getMethod();
            if ("OPTIONS".equals(requestMethod)) {
                return true;
            }
            if (!(handler instanceof HandlerMethod)) {
                return true;
            }
//            // 从切点上获取目标方法
//            HandlerMethod handlerMethod = (HandlerMethod) handler;
//            Method method = handlerMethod.getMethod();
//            // 若目标方法忽略了安全性检查，则直接调用目标方法
//            if (method.isAnnotationPresent(IgnoreToken.class)) {
//                return true;
//            }
            // 校验token
            String token = request.getHeader("X-Token");
            log.info("传入的token值:{}", token);
            if (StringUtils.isEmpty(token) || !TokenUtil.isValid(token)) {
                log.info("token校验错误");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                if(uri.startsWith("/alarm") || uri.startsWith("/manager")) {
                    response.getWriter().append(JSON.toJSONString(new AuthorizedBody("401", "token Check the error")));
                } else {
                    response.sendRedirect("/");
                }
                return false;
            }
            WebToeknContext.initToken(token);
            RoleCheckContext.checkUserRole(token);
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
