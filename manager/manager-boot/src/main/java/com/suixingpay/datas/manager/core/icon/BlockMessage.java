/**
 * 
 */
package com.suixingpay.datas.manager.core.icon;

/**
 * @author guohongjian[guo_hj@suixingpay.com] 首页块内消息
 */
public class BlockMessage {

    public BlockMessage(String serial, String rowText, String linkUrl) {
        this.serial = serial;
        this.rowText = rowText;
        this.linkUrl = linkUrl;
    }

    private String serial;

    private String rowText;

    private String linkUrl;

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getRowText() {
        return rowText;
    }

    public void setRowText(String rowText) {
        this.rowText = rowText;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

}
