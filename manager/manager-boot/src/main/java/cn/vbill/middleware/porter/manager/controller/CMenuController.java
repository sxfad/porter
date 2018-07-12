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

package cn.vbill.middleware.porter.manager.controller;

import cn.vbill.middleware.porter.manager.core.entity.CMenu;
import cn.vbill.middleware.porter.manager.core.init.MenuUtils;
import cn.vbill.middleware.porter.manager.core.init.ResourceUtils;
import cn.vbill.middleware.porter.manager.service.CMenuService;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.vbill.middleware.porter.manager.web.message.ResponseMessage.ok;

/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: 付紫钲
 * @date: 2018/4/16
 */
@Api(description = "菜单管理")
@RestController
@RequestMapping("/manager/cmenu")
public class CMenuController {

    @Autowired
    private CMenuService cMenuService;

    /**
     * 查询全部菜单
     *
     * @author FuZizheng
     * @date 2018/4/16 下午2:31
     * @param: [roleCode]
     * @return: ResponseMessage
     */
    @GetMapping
    @ApiOperation(value = "查询全部菜单", notes = "查询全部菜单")
    public ResponseMessage findAll() {
        CMenu cMenu = cMenuService.findAll();
        return ok(cMenu);
    }

    /**
     * 根据fatherCode查询
     *
     * @author FuZizheng
     * @date 2018/4/16 下午3:21
     * @param: []
     * @return: ResponseMessage
     */
    @GetMapping("/findByFatherCode")
    @ApiOperation(value = "根据fatherCode查询", notes = "根据fatherCode查询")
    public ResponseMessage findByFatherCode(@RequestParam(value = "fatherCode", required = false) String fatherCode) {
        List<CMenu> menus = cMenuService.findByFatherCode(fatherCode);
        return ok(menus);
    }

    /**
     * 根据主键查询
     *
     * @author FuZizheng
     * @date 2018/4/16 下午3:43
     * @param: [id]
     * @return: ResponseMessage
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据主键查询", notes = "根据主键查询")
    public ResponseMessage findById(@PathVariable(value = "id", required = false) Long id) {
        CMenu cMenu = cMenuService.findById(id);
        return ok(cMenu);
    }

    /**
     * 新增
     *
     * @author FuZizheng
     * @date 2018/4/16 下午3:27
     * @param: [cMenu]
     * @return: ResponseMessage
     */
    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage insert(@RequestBody CMenu cMenu) {
        Integer number = cMenuService.insert(cMenu);
        return ok(number);
    }

    /**
     * 修改
     *
     * @author FuZizheng
     * @date 2018/4/16 下午3:41
     * @param: [id, cMenu]
     * @return: ResponseMessage
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "修改", notes = "修改")
    public ResponseMessage update(@PathVariable("id") Long id, @RequestBody CMenu cMenu) {
        Integer number = cMenuService.update(id, cMenu);
        return ok(number);
    }

    /**
     * 逻辑删除
     *
     * @author FuZizheng
     * @date 2018/4/16 下午3:51
     * @param: []
     * @return: ResponseMessage
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "逻辑删除", notes = "逻辑删除")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        Integer number = cMenuService.delete(id);
        return ok(number);
    }

    @GetMapping("/ref")
    @ApiOperation(value = "重新加载初始化数据", notes = "重新加载初始化数据")
    public ResponseMessage reflash() {
        //清空节点id与节点名称的对照关系
        ResourceUtils.NODEIDNAME_MAP.clear();
        //清空任务id与任务名称的对照关系
        ResourceUtils.JOBNAME_MAP.clear();
        //清空菜单权限
        MenuUtils.ROLE_MENU.clear();
        //清空角色-菜单-路径关系
        MenuUtils.ROLE_MENU_URL.clear();

        ResourceUtils.getInstance().init();
        MenuUtils.getInstance().init();

        return ok();
    }

}
