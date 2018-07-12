package cn.vbill.middleware.porter.manager.service.impl;

import cn.vbill.middleware.porter.manager.core.icon.HomeBlockResult;
import cn.vbill.middleware.porter.manager.core.mapper.HomeMapper;
import cn.vbill.middleware.porter.manager.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author: 付紫钲
 * @date: 2018/4/26
 */
@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private HomeMapper homeMapper;

    @Override
    public HomeBlockResult bolck() {
        HomeBlockResult homeBlockResult = homeMapper.block();
        return homeBlockResult;
    }
}
