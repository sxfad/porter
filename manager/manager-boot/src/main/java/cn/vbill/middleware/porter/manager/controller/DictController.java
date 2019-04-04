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

import cn.vbill.middleware.porter.manager.service.DictService;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static cn.vbill.middleware.porter.manager.web.message.ResponseMessage.ok;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 */
@Api(description = "字典数据")
@RestController
@RequestMapping("/manager/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    /**
     * dict
     *
     * @date 2018/8/9 下午4:22
     * @param: []
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @GetMapping("/all")
    @ApiOperation(value = "全部字典", notes = "全部字典")
    public ResponseMessage dict() {
        Map<String, Map<String, Object>> map = dictService.dictAll();
        return ok(map);
    }

    /**
     * dictType
     *
     * @date 2018/8/9 下午4:22
     * @param: [type]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @GetMapping("/{type}")
    @ApiOperation(value = "标识字典", notes = "标识字典")
    public ResponseMessage dictType(@PathVariable("type") String type) {
        Map<String, Object> map = dictService.dictByType(type);
        return ok(map);
    }

    /**
     * 获取任务权限操作字典
     *
     * @author MurasakiSeiFu
     * @date 2019-04-03 14:08
     * @param: []
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @GetMapping("/getControlType")
    @ApiOperation(value = "获取任务权限操作字典", notes = "获取任务权限操作字典")
    public ResponseMessage dictControlType() {
        Map<String, Object> map = dictService.dictControlType();
        return ok(map);
    }
}
