package com.suixingpay.datas.manager.service.impl;

import com.suixingpay.datas.manager.core.icon.HomeBlockResult;
import com.suixingpay.datas.manager.core.mapper.HomeMapper;
import com.suixingpay.datas.manager.service.HomeService;
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
