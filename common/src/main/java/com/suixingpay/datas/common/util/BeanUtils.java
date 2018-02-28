/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月07日 18:09
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月07日 18:09
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月07日 18:09
 */
public class BeanUtils {

    public static void copyProperties(Map<String, String> source, Object target, String... ignoreProperties)
            throws InvocationTargetException, IllegalAccessException {
        PropertyDescriptor[] targetPds = org.springframework.beans.BeanUtils.getPropertyDescriptors(target.getClass());
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);
        for (PropertyDescriptor targetPd : targetPds) {
            String columnName = targetPd.getName();
            if ((ignoreList == null || !ignoreList.contains(columnName))) {
                if (source.containsKey(columnName)) {
                    Object value = source.get(columnName);
                    org.apache.commons.beanutils.BeanUtils.copyProperty(target, columnName, value);
                }
            }
        }
    }
}
