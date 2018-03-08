package com.suixingpay.datas.manager.web.page;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
public class Page<T> {

    public Page() {

    }

    public Page(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    private int pageNo = 1; // 当前页

    private int pageSize = 10; // 一页内记录数

    private long totalItems = 0; // 总数

    private List<T> result = new ArrayList<T>(); // 当前页数据

    public int getOffset() {
        return ((pageNo - 1) * pageSize);
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

    public long getTotalItems() {
        if (totalItems < 0) {
            return -1;
        }
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public long getTotalPages() {
        long count = totalItems / pageSize;
        if (totalItems % pageSize > 0) {
            count++;
        }
        return count;
    }

}
