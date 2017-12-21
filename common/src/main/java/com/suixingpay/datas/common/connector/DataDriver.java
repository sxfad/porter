package com.suixingpay.datas.common.connector;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 10:44
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import java.util.Map;

/**
 * 源数据库连接信息
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 10:44
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 10:44
 */
public class DataDriver {
    private String driverClassName;
    private String url;
    private String userName;
    private String password;
    //驱动类型
    private DataDriverType type;
    //扩展属性，与DataDriverType关联
    private Map<String,String> extendAttr;
    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DataDriverType getType() {
        return type;
    }

    public void setType(DataDriverType type) {
        this.type = type;
    }

    public Map<String, String> getExtendAttr() {
        return extendAttr;
    }

    public void setExtendAttr(Map<String, String> extendAttr) {
        this.extendAttr = extendAttr;
    }
}
