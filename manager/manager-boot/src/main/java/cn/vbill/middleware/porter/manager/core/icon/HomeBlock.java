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

package cn.vbill.middleware.porter.manager.core.icon;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页块
 * 
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
public class HomeBlock {

    public HomeBlock(String title, List<BlockMessage> messages) {
        this.title = title;
        this.messages = messages;
    }

    private String title;

    private List<BlockMessage> messages = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<BlockMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<BlockMessage> messages) {
        this.messages = messages;
    }

}
