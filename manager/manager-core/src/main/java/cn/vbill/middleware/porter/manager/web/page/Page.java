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

package cn.vbill.middleware.porter.manager.web.page;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <T>
 * @author guohongjian[guo_hj@suixingpay.com]
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

    /**
     * getTotalItems
     *
     * @date 2018/8/9 下午3:37
     * @param: []
     * @return: long
     */
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

    /**
     * getTotalPages
     *
     * @date 2018/8/9 下午3:37
     * @param: []
     * @return: long
     */
    public long getTotalPages() {
        long count = totalItems / pageSize;
        if (totalItems % pageSize > 0) {
            count++;
        }
        return count;
    }

}
