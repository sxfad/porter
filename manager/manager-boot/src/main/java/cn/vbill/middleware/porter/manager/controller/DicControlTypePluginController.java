/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: FairyHood
 * @date: 2019-04-02 10:58:29
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package cn.vbill.middleware.porter.manager.controller;

import cn.vbill.middleware.porter.manager.core.entity.DicControlTypePlugin;
import cn.vbill.middleware.porter.manager.service.DicControlTypePluginService;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.vbill.middleware.porter.manager.web.message.ResponseMessage.ok;

/**
 * 操作类型字典 controller控制器
 *
 * @author: FairyHood
 * @date: 2019-04-02 10:58:29
 * @version: V1.0-auto
 * @review: FairyHood/2019-04-02 10:58:29
 */
@Api(description = "操作类型字典管理")
@RestController
@RequestMapping("/manager/controltypeplugin")
public class DicControlTypePluginController {

    @Autowired
    protected DicControlTypePluginService dicControlTypePluginService;

    /**
     * 获取全部操作类型字典
     *
     * @author MurasakiSeiFu
     * @date 2019-04-03 14:22
     * @param: []
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @GetMapping
    @ApiOperation(value = "获取全部操作类型字典", notes = "获取全部操作类型字典")
    public ResponseMessage findAll() {
        List<DicControlTypePlugin> list = dicControlTypePluginService.findAll();
        return ok(list);
    }


}
