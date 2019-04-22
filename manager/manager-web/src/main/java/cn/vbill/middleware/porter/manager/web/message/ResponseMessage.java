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

package cn.vbill.middleware.porter.manager.web.message;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import cn.vbill.middleware.porter.manager.core.enums.DataSignEnum;
import cn.vbill.middleware.porter.manager.core.util.DateFormatUtils;

/**
 * 反馈
 *
 * @author guohongjian[guo_hj@suixingpay.com]
 */
public class ResponseMessage implements Serializable {

    private static final long serialVersionUID = 8992436576262574064L;

    private Logger logger = LoggerFactory.getLogger(ResponseMessage.class);

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 反馈数据
     */
    private Object data;

    /** 数据标识. */
    private DataSignEnum dataSign;

    /**
     * 反馈信息
     */
    private String message;

    /**
     * 响应码
     */
    private int code;

    /**
     * 过滤字段：指定需要序列化的字段
     */
    private transient Map<Class<?>, Set<String>> includes;

    /**
     * 过滤字段：指定不需要序列化的字段
     */
    private transient Map<Class<?>, Set<String>> excludes;

    private transient boolean onlyData;

    private transient String callback;

    /**
     * toMap
     *
     * @date 2018/8/9 下午3:30
     * @param: []
     * @return: java.util.Map<java.lang.String,java.lang.Object>
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("success", this.success);
        if (data != null) {
            map.put("data", this.getData());
        }
        if (message != null) {
            map.put("message", this.getMessage());
        }
        map.put("code", this.getCode());
        return map;
    }

    protected ResponseMessage(String message) {
        this.code = 500;
        this.message = message;
        this.success = false;
    }

    protected ResponseMessage(boolean success, Object data) {
        this.code = success ? 200 : 500;
        this.data = data;
        this.success = success;
    }

    protected ResponseMessage(boolean success, Object data, DataSignEnum dataSign) {
        this.code = success ? 200 : 500;
        this.data = data;
        this.success = success;
        this.dataSign = dataSign;
    }

    protected ResponseMessage(boolean success, Object data, int code) {
        this(success, data);
        this.code = code;
    }

    protected ResponseMessage(boolean success, Object data, int code, DataSignEnum dataSign) {
        this(success, data);
        this.code = code;
        this.dataSign = dataSign;
    }

    public ResponseMessage include(Class<?> type, String... fields) {
        return include(type, Arrays.asList(fields));
    }

    public ResponseMessage include(Class<?> type, Collection<String> fields) {
        if (includes == null) {
            includes = new HashMap<>();
        }
        if (fields == null || fields.isEmpty()) {
            return this;
        }
        fields.forEach(field -> {
            if (field.contains(".")) {
                String[] tmp = field.split("[.]", 2);
                try {
                    Field field1 = type.getDeclaredField(tmp[0]);
                    if (field1 != null) {
                        include(field1.getType(), tmp[1]);
                    }
                } catch (Throwable e) {
                    logger.info("%s", e);
                }
            } else {
                getStringListFormMap(includes, type).add(field);
            }
        });
        return this;
    }

    public ResponseMessage exclude(Class<?> type, Collection<String> fields) {
        if (excludes == null) {
            excludes = new HashMap<>();
        }
        if (fields == null || fields.isEmpty()) {
            return this;
        }
        fields.forEach(field -> {
            if (field.contains(".")) {
                String[] tmp = field.split("[.]", 2);
                try {
                    Field field1 = type.getDeclaredField(tmp[0]);
                    if (field1 != null) {
                        exclude(field1.getType(), tmp[1]);
                    }
                } catch (Throwable e) {
                    logger.info("%s", e);
                }
            } else {
                getStringListFormMap(excludes, type).add(field);
            }
        });
        return this;
    }

    public ResponseMessage exclude(Collection<String> fields) {
        if (excludes == null) {
            excludes = new HashMap<>();
        }
        if (fields == null || fields.isEmpty()) {
            return this;
        }
        Class<?> type;
        if (data != null) {
            type = data.getClass();
        } else {
            return this;
        }
        exclude(type, fields);
        return this;
    }

    public ResponseMessage include(Collection<String> fields) {
        if (includes == null) {
            includes = new HashMap<>();
        }
        if (fields == null || fields.isEmpty()) {
            return this;
        }
        Class<?> type;
        if (data != null) {
            type = data.getClass();
        } else {
            return this;
        }
        include(type, fields);
        return this;
    }

    public ResponseMessage exclude(Class<?> type, String... fields) {
        return exclude(type, Arrays.asList(fields));
    }

    public ResponseMessage exclude(String... fields) {
        return exclude(Arrays.asList(fields));
    }

    public ResponseMessage include(String... fields) {
        return include(Arrays.asList(fields));
    }

    protected Set<String> getStringListFormMap(Map<Class<?>, Set<String>> map, Class<?> type) {
        Set<String> list = map.get(type);
        if (list == null) {
            list = new HashSet<>();
            map.put(type, list);
        }
        return list;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public ResponseMessage setData(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONStringWithDateFormat(this, DateFormatUtils.PATTERN_DEFAULT_ON_SECOND);
    }

    public int getCode() {
        return code;
    }

    public ResponseMessage setCode(int code) {
        this.code = code;
        return this;
    }

    public static ResponseMessage fromJson(String json) {
        return JSON.parseObject(json, ResponseMessage.class);
    }

    public Map<Class<?>, Set<String>> getExcludes() {
        return excludes;
    }

    public Map<Class<?>, Set<String>> getIncludes() {
        return includes;
    }

    public ResponseMessage onlyData() {
        setOnlyData(true);
        return this;
    }

    public void setOnlyData(boolean onlyData) {
        this.onlyData = onlyData;
    }

    public boolean isOnlyData() {
        return onlyData;
    }

    public ResponseMessage callback(String callback) {
        this.callback = callback;
        return this;
    }

    public static ResponseMessage ok() {
        return ok(null);
    }

    public static ResponseMessage ok(Object data) {
        return new ResponseMessage(true, data);
    }

    public static ResponseMessage ok(Object data, DataSignEnum sign) {
        return new ResponseMessage(true, data, sign);
    }

    public static ResponseMessage created(Object data) {
        return new ResponseMessage(true, data, 201);
    }

    public static ResponseMessage created(Object data, DataSignEnum sign) {
        return new ResponseMessage(true, data, 201, sign);
    }

    public static ResponseMessage error(String message) {
        return new ResponseMessage(message);
    }

    public static ResponseMessage error(String message, int code) {
        return new ResponseMessage(message).setCode(code);
    }

    public String getCallback() {
        return callback;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataSignEnum getDataSign() {
        return dataSign;
    }

    public void setDataSign(DataSignEnum dataSign) {
        this.dataSign = dataSign;
    }

}
