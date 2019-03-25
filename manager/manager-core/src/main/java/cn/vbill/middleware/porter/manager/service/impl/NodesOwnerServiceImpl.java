/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: FairyHood
 * @date: 2019-03-20 16:29:06
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package cn.vbill.middleware.porter.manager.service.impl;

import cn.vbill.middleware.porter.manager.core.entity.NodesOwner;
import cn.vbill.middleware.porter.manager.core.mapper.NodesOwnerMapper;
import cn.vbill.middleware.porter.manager.service.NodesOwnerService;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 节点所有权控制表 服务实现类
 *
 * @author: FairyHood
 * @date: 2019-03-20 16:29:06
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-20 16:29:06
 */
@Service
public class NodesOwnerServiceImpl implements NodesOwnerService {

    @Autowired
    private NodesOwnerMapper nodesOwnerMapper;

    @Override
    public Integer insert(NodesOwner nodesOwner) {
        return nodesOwnerMapper.insert(nodesOwner);
    }

    @Override
    public Integer update(Long id, NodesOwner nodesOwner) {
        return nodesOwnerMapper.update(id, nodesOwner);
    }

    @Override
    public Integer delete(Long id) {
        return nodesOwnerMapper.delete(id);
    }

    @Override
    public NodesOwner selectById(Long id) {
        return nodesOwnerMapper.selectById(id);
    }

    @Override
    public Page<NodesOwner> page(Page<NodesOwner> page) {
        Integer total = nodesOwnerMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(nodesOwnerMapper.page(page, 1));
        }
        return page;
    }

}
