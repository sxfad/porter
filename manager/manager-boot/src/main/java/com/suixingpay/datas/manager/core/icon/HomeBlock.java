/**
 * 
 */
package com.suixingpay.datas.manager.core.icon;

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
