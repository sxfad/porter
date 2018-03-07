/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 11:53
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.plugin.consumer.canal;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.suixingpay.datas.common.client.ConsumeClient;
import com.suixingpay.datas.common.dic.ConsumerPlugin;
import com.suixingpay.datas.node.core.consumer.AbstractDataConsumer;
import com.suixingpay.datas.node.core.event.s.MessageEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * canal row消费端
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月06日 11:27
 * @version: V2.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年03月06日 11:27
 */
public class CanalConsumer extends AbstractDataConsumer {
    public List<MessageEvent> doFetch() {
        return consumeClient.fetch(new ConsumeClient.FetchCallback<MessageEvent, Object>() {
            @Override
            public <F, O> List<F> acceptAll(O o) {
                List<F> events = new ArrayList<>();
                Message msg = (Message) o;
                for (CanalEntry.Entry entry : msg.getEntries()) {
                    JSONObject header = new JSONObject();
                    header.put("batchId", msg.getId());
                    header.put("offset", entry.getHeader().getLogfileOffset());
                    header.put("logfileName", entry.getHeader().getLogfileName());
                    List<F> convertedObj = (List<F>) converter.convertList(header, entry);
                    if (null != convertedObj && !convertedObj.isEmpty()) {
                        events.addAll(convertedObj);
                    }
                }
                return events;
            }
        });
    }

    @Override
    protected String getPluginName() {
        return ConsumerPlugin.CANAL.getCode();
    }
}
