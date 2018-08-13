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

package cn.vbill.middleware.porter.common.util;

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

    /**
     * copyProperties
     * @param source
     * @param target
     * @param ignoreProperties
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
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
