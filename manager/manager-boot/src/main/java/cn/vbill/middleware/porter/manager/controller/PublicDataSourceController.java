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

import static cn.vbill.middleware.porter.manager.web.message.ResponseMessage.ok;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.vbill.middleware.porter.common.cluster.ClusterProviderProxy;
import cn.vbill.middleware.porter.common.cluster.impl.AbstractClusterListener;
import cn.vbill.middleware.porter.common.task.config.PublicSourceConfig;
import cn.vbill.middleware.porter.manager.core.entity.PublicDataSource;
import cn.vbill.middleware.porter.manager.core.enums.DataSignEnum;
import cn.vbill.middleware.porter.manager.service.PublicDataSourceService;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;
import cn.vbill.middleware.porter.manager.web.page.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 公共数据源配置表 controller控制器
 *
 * @author: FairyHood
 * @date: 2019-03-13 09:58:24
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-13 09:58:24
 */
@Api(description = "公共数据源配置表管理")
@RestController
@RequestMapping("/manager/pdse")
public class PublicDataSourceController {

    private Logger log = LoggerFactory.getLogger(PublicDataSourceController.class);

    @Autowired
    protected PublicDataSourceService publicDataSourceService;

    /**
     * 新增
     *
     * @author FuZizheng
     * @date 2019-03-13 10:14
     * @param: [publicDataSource]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody PublicDataSource publicDataSource) {
        Integer number = publicDataSourceService.insert(publicDataSource);
        return ok(number);
    }

    /**
     * 修改
     *
     * @author FuZizheng
     * @date ` 10:14
     * @param: [id,
     *             publicDataSource]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "修改", notes = "修改")
    public ResponseMessage update(@PathVariable("id") Long id, @RequestBody PublicDataSource publicDataSource) {
        Integer number = publicDataSourceService.update(id, publicDataSource);
        return ok(number);
    }

    /**
     * 删除
     *
     * @author FuZizheng
     * @date 2019-03-13 10:14
     * @param: [id]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        publicDataSourceService.updateCancel(id);
        return ok();
    }

    /**
     * 查询明细
     *
     * @author FuZizheng
     * @date 2019-03-13 10:14
     * @param: [id]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info(@PathVariable("id") Long id) {
        PublicDataSource publicDataSource = publicDataSourceService.selectById(id);
        return ok(publicDataSource);
    }

    /**
     * 查询列表
     *
     * @author FuZizheng
     * @date 10:14
     * @param: [pageNum,
     *             pageSize]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @GetMapping
    @ApiOperation(value = "查询列表", notes = "查询列表")
    public ResponseMessage list(@RequestParam(value = "pageNum", required = true) Integer pageNum,
            @RequestParam(value = "pageSize", required = true) Integer pageSize,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "ipsite", required = false) String ipsite) {
        Page<PublicDataSource> page = publicDataSourceService.page(new Page<PublicDataSource>(pageNum, pageSize), id,
                code, name, ipsite);
        return ok(page, DataSignEnum.PUBLICDATASOURCE);
    }

    /**
     * 解析特殊配置
     * 
     * @param jobTasks
     * @return
     */
    @PostMapping(value = "/dealxml")
    @ApiOperation(value = "解析字符串", notes = "解析字符串")
    public ResponseMessage dealxml(@RequestBody PublicDataSource publicDataSource) {
        String xmlText = publicDataSource.getXmlText();
        log.info("dealxml传入字符串:[{}]", xmlText);
        try {
            String xmlTextStr = java.net.URLDecoder.decode(xmlText, "UTF-8");
            log.info("dealxml转移后字符串:[{}]", xmlTextStr);
            PublicSourceConfig config = publicDataSourceService.dealxml(xmlTextStr);
            log.info("dealxml解析后字符串:[{}]", JSON.toJSON(config));
            return ok(config);
        } catch (UnsupportedEncodingException e) {
            log.error("dealxml输入数据解析失败!", e);
            return ResponseMessage.error("dealxml输入数据解析失败!");
        }
    }

    /**
     * 推送数据源
     * 
     * @param id
     * @return
     */
    @PostMapping(value = "/push/{id}")
    @ApiOperation(value = "推送公共数据源", notes = "推送公共数据源")
    public ResponseMessage zkpush(@PathVariable("id") Long id) {
        PublicDataSource publicDataSource = publicDataSourceService.selectById(id);
        PublicSourceConfig config = JSONObject.parseObject(publicDataSource.getJsonText(), PublicSourceConfig.class);
        config.setCode(publicDataSource.getCode());
        try {
            ClusterProviderProxy.INSTANCE.broadcastEvent(client -> {
                String configPath = AbstractClusterListener.BASE_CATALOG + "/datesource/" + publicDataSource.getCode();
                if (!StringUtils.isBlank(configPath)) {
                    client.changeData(configPath, false, false, JSON.toJSONString(config));
                }
            });
            publicDataSourceService.updatePush(id, 1);
            log.info("推送公共数据源[{}]信息到zk成功,详细信息[{}]!", id, JSON.toJSONString(config));
        } catch (Exception e) {
            log.error("推送公共数据源[{}]信息到zk失败,请关注！", id, e);
            return ResponseMessage.error("推送公共数据源信息失败，请关注！");
        }
        return ok(id);
    }

    /**
     * 回收公共数据源
     * 
     * @param id
     * @return
     */
    @PostMapping(value = "/takeback/{id}")
    @ApiOperation(value = "回收公共数据源", notes = "回收公共数据源")
    public ResponseMessage takeback(@PathVariable("id") Long id) {
        PublicDataSource publicDataSource = publicDataSourceService.selectById(id);
        PublicSourceConfig config = JSONObject.parseObject(publicDataSource.getJsonText(), PublicSourceConfig.class);
        config.setCode(publicDataSource.getCode());
        try {
            ClusterProviderProxy.INSTANCE.broadcastEvent(client -> {
                String configPath = AbstractClusterListener.BASE_CATALOG + "/datesource/" + publicDataSource.getCode();
                if (!StringUtils.isBlank(configPath)) {
                    client.delete(configPath);
                }
            });
            publicDataSourceService.updatePush(id, -1);
            log.info("回收公共数据源[{}]信息到zk成功,详细信息[{}]!", id, JSON.toJSONString(config));
        } catch (Exception e) {
            log.error("回收公共数据源[{}]信息到zk失败,请关注！", id, e);
            return ResponseMessage.error("回收公共数据源信息失败，请关注！");
        }
        return ok(id);
    }
}
