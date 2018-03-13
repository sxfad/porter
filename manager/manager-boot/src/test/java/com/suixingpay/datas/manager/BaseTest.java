package com.suixingpay.datas.manager;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = ManagerBootApplication.class)
@ActiveProfiles("sample")// 开发环境
public class BaseTest {
}