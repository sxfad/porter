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

package cn.vbill.middleware.porter.common.warning.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 10:43
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月23日 10:43
 */
@NoArgsConstructor
@Getter @Setter
public class WarningMessage {
    private String title;
    private String content;
    private WarningErrorCode errorCode;
    private WarningOwner receiver;
    private List<WarningReceiver> copy = new ArrayList<>();

    public WarningMessage(String content, WarningErrorCode errorCode, WarningOwner receiver) {
        this(null, content, errorCode, receiver);
    }

    public WarningMessage(String title, String content, WarningErrorCode errorCode, WarningOwner receiver) {
        this.content = content;
        this.errorCode = errorCode;
        this.receiver =  receiver;
        this.title = StringUtils.isNotBlank(title) ? title : buildTitle(null);
    }

    public WarningMessage bindCopy(List<WarningReceiver> copy) {
        this.copy.addAll(copy);
        return this;
    }

    private String buildTitle(String prefix) {
        WarningReceiver receiver = null != this.receiver && null != this.receiver.getOwner() ? this.receiver.getOwner() : new WarningReceiver("系统管理员", "", "00000000000");
        StringBuffer sb = new StringBuffer();
        if (null != prefix && !prefix.isEmpty()) sb.append("[").append(prefix).append("]");
        sb.append("[").append(errorCode.name()).append("(").append(errorCode.getDesc()).append(")]");
        sb.append(receiver.getRealName()).append(receiver.getPhone());
        return sb.toString();
    }

    public WarningMessage appendTitlePrefix(List<String> prefix) {
        StringBuffer sb = new StringBuffer();
        if (null != prefix && !prefix.isEmpty()) sb.append("[").append(StringUtils.join(prefix, "-")).append("]");
        sb.append(title);
        title = sb.toString();
        return this;
    }
}
