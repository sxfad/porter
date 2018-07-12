/*
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

package cn.vbill.middleware.porter.manager.exception;

/**
 * 异常
 *
 * @author guohongjian[guo_hj@suixingpay.com]
 */
public class ExceptionCode {

    /**
     * 404异常.
     */
    public static final int EXCEPTION_404 = 404;

    /**
     * 业务异常.
     */
    public static final int EXCEPTION_SERVICE = 400;

    /**
     * 系统异常.
     */
    public static final int EXCEPTION_SYSTEM = 199;

    /**
     * 校验异常.
     */
    public static final int EXCEPTION_JSRVALID = 303;

    /**
     * 幂等校验.
     */
    public static final int EXCEPTION_IDEMPOTENT = 304;

    /**
     * 登陆错误.
     */
    public static final int EXCEPTION_LOGIN = 901;

    /**
     * 数据源异常
     */
    public static final int EXCEPTION_DATASOURCE = 902;

}
