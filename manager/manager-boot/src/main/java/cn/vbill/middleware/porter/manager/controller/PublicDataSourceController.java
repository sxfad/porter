/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: FairyHood 
 * @date: 2019-03-13 09:58:24  
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package cn.vbill.middleware.porter.manager.controller;

import static com.suixingpay.takin.web.message.ResponseMessage.ok;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.suixingpay.takin.common.exception.NotFoundException;
import com.suixingpay.takin.common.validator.annotation.JsrValid;
import com.suixingpay.takin.common.validator.annotation.JsrValidator;
import com.suixingpay.takin.mybatis.mapper.EntityWrapper;
import com.suixingpay.takin.mybatis.plugin.Page;
import com.suixingpay.takin.web.log.config.AccessLogger;
import com.suixingpay.takin.web.message.ResponseMessage;
import io.swagger.annotations.ApiOperation;

import cn.vbill.middleware.porter.manager.core.entity.PublicDataSource;
import cn.vbill.middleware.porter.manager.service.PublicDataSourceService;
import com.suixingpay.takin.web.CommonController;

import io.swagger.annotations.Api;

 /**  
 * 公共数据源配置表 controller控制器
 * @author: FairyHood
 * @date: 2019-03-13 09:58:24
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-13 09:58:24
 */
@Api(description="公共数据源配置表管理")
@RestController
@RequestMapping("/publicdatasource")
public class PublicDataSourceController extends CommonController{
	
    @Autowired
    protected PublicDataSourceService publicDataSourceService;
    
    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody@JsrValidator PublicDataSource publicDataSource) {
        Integer number = publicDataSourceService.insert(publicDataSource);
        return ok(number);
    }
    
    @PutMapping("/{id}")
    @ApiOperation(value = "修改", notes = "修改")
    public ResponseMessage update(@PathVariable("id") Long id, @RequestBody@JsrValidator PublicDataSource publicDataSource) {
        Integer number = publicDataSourceService.updateByPk(publicDataSource);
        return ok(number);
    }
    
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        publicDataSourceService.deleteByPk(id);
        return ok();
    }
    
    @GetMapping("/{id}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info(@PathVariable("id") Long id) {
        PublicDataSource publicDataSource = publicDataSourceService.selectById(id);
        return ok(publicDataSource);
    }

    @GetMapping
    @ApiOperation(value = "查询列表", notes = "查询列表")
    public ResponseMessage list(@RequestParam(value = "pageNum", required = true) Integer pageNum,
                                @RequestParam(value = "pageSize", required = true) Integer pageSize) {
        Page<PublicDataSource> page = publicDataSourceService.page(new Page<PublicDataSource>(pageNo, pageSize));
        return ok(page);
    }
    
}
