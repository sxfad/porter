/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 11:53
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.plugin.loader.kafka;


import com.suixingpay.datas.common.dic.LoaderPlugin;
import com.suixingpay.datas.common.exception.TaskStopTriggerException;
import com.suixingpay.datas.node.core.event.etl.ETLBucket;
import com.suixingpay.datas.node.core.loader.SubmitStatObject;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 11:53
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 11:53
 */
public class KafkaAsyncLoader extends KafkaLoader {
    @Override
    protected String getPluginName() {
        return LoaderPlugin.KAFKA_ASYNC.getCode();
    }

    @Override
    public Pair<Boolean, List<SubmitStatObject>> load(ETLBucket bucket) throws TaskStopTriggerException {
        return storeData(bucket, false);
    }
}
