/**
 *
 */
package com.suixingpay.datas.manager.web.page;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 */
public class PageVo {

    private int pageNo = 1; // 当前页

    private int pageSize = 10; // 一页内记录数

    public PageVo(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
