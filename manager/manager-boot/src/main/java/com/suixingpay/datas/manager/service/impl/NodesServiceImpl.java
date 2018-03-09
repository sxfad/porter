/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: FairyHood 
 * @date: 2018-03-07 13:40:30  
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.manager.service.impl;

import com.suixingpay.datas.manager.core.entity.Nodes;
import com.suixingpay.datas.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suixingpay.datas.manager.core.mapper.NodesMapper;
import com.suixingpay.datas.manager.service.NodesService;

/**
 * 节点信息表 服务实现类
 * 
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
@Service
public class NodesServiceImpl implements NodesService {

    @Autowired
    private NodesMapper nodesMapper;

    @Override
    public Integer insert(Nodes nodes) {
        return nodesMapper.insert(nodes);
    }

    @Override
    public Integer update(Long id, Nodes nodes) {
        return nodesMapper.update(id, nodes);
    }

    @Override
    public Integer delete(Long id) {
        return nodesMapper.delete(id);
    }

    @Override
    public Nodes selectById(Long id) {
        return nodesMapper.selectById(id);
    }

    @Override
    public Page<Nodes> page(Page<Nodes> page) {
        Integer total = nodesMapper.pageAll(1);
        if(total>0) {
            page.setTotalItems(total);
            page.setResult(nodesMapper.page(page, 1));
        }
        return page;
    }
}
