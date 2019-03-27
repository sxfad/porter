<<<<<<< HEAD
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
=======
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
>>>>>>> branch 'master' of git@192.168.120.68:root/suixingpay-porter.git
