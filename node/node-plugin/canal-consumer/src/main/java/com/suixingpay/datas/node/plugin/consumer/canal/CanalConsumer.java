/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 11:53
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.plugin.consumer.canal;


import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.suixingpay.datas.common.client.impl.CanalClient;
import com.suixingpay.datas.common.consumer.ConsumeClient;
import com.suixingpay.datas.common.dic.ConsumerPlugin;
import com.suixingpay.datas.common.exception.TaskStopTriggerException;
import com.suixingpay.datas.node.core.consumer.AbstractDataConsumer;
import com.suixingpay.datas.node.core.event.s.MessageEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * canal row消费端
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月06日 11:27
 * @version: V2.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年03月06日 11:27
 */
public class CanalConsumer extends AbstractDataConsumer {
    public List<MessageEvent> doFetch() throws TaskStopTriggerException, InterruptedException {
        return consumeClient.fetch(new ConsumeClient.FetchCallback<MessageEvent, Object>() {
            @Override
            public <F, O> List<F> acceptAll(O o) {
                List<MessageEvent> events = new ArrayList<>();
                Message msg = (Message) o;

                //批次消息同步提交点
                CanalClient.CanalPosition bucketHeader = null;
                List<CanalEntry.Entry> endEntries = msg.getEntries().stream().filter(e -> e.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND)
                        .collect(Collectors.toList());

                if (!endEntries.isEmpty()) {
                    CanalEntry.Entry lastEndEntry = endEntries.get(endEntries.size() - 1);
                    bucketHeader = new CanalClient.CanalPosition(msg.getId(), lastEndEntry.getHeader().getLogfileOffset(),
                            lastEndEntry.getHeader().getLogfileName());
                } else {
                    bucketHeader = new CanalClient.CanalPosition(msg.getId());
                }

                for (CanalEntry.Entry entry : msg.getEntries()) {
                    //事务消息同步点
                    CanalClient.CanalPosition rowHeader = new CanalClient.CanalPosition(msg.getId(), entry.getHeader().getLogfileOffset(),
                            entry.getHeader().getLogfileName());

                    List<MessageEvent> convertedObj = converter.convertList(bucketHeader, rowHeader, entry);
                    if (null != convertedObj && !convertedObj.isEmpty()) {
                        events.addAll(convertedObj);
                    }
                }
                return (List<F>) events;
            }
        });
    }

    @Override
    protected String getPluginName() {
        return ConsumerPlugin.CANAL.getCode();
    }
}
