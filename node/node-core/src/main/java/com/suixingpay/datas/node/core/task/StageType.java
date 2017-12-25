package com.suixingpay.datas.node.core.task;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 10:56
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

/**
 * 工作的阶段
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 10:56
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月24日 10:56
 */
public enum  StageType {
    SELECT, EXTRACT, TRANSFORM, LOAD, DB_CHECK;
    private StageType() {

    }
    public boolean isSelect() {
        return this.equals(StageType.SELECT);
    }

    public boolean isExtract() {
        return this.equals(StageType.EXTRACT);
    }

    public boolean isTransform() {
        return this.equals(StageType.TRANSFORM);
    }

    public boolean isLoad() {
        return this.equals(StageType.LOAD);
    }

    public boolean isDbCheck() {
        return this.equals(StageType.DB_CHECK);
    }
}
