package com.suixingpay.datas.manager.controller;

import com.suixingpay.datas.manager.core.entity.LogGrade;
import com.suixingpay.datas.manager.service.LogGradeService;
import com.suixingpay.datas.manager.web.message.ResponseMessage;
import com.suixingpay.datas.manager.web.page.Page;
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

import static com.suixingpay.datas.manager.web.message.ResponseMessage.ok;

/**
 * 日志级别表 controller控制器
 *
 * @author: FairyHood
 * @date: 2018-03-07 17:26:55
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 17:26:55
 */
@Api(description = "日志级别表管理")
@RestController
@RequestMapping("/loggrade")
public class LogGradeController {

    @Autowired
    protected LogGradeService logGradeService;

    /**
     * 新增
     *
     * @author FuZizheng
     * @date 2018/3/16 上午11:03
     * @param: [logGrade]
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody LogGrade logGrade) {
        Integer number = logGradeService.insert(logGrade);
        return ok(number);
    }

    /**
     * 查询
     *
     * @author FuZizheng
     * @date 2018/3/16 上午11:30
     * @param: []
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @GetMapping
    @ApiOperation(value = "查询当前日志级别", notes = "若无数据，默认为INFO")
    public ResponseMessage info() {
        LogGrade logGrade = logGradeService.select();
        return ok(logGrade);
    }

    /*@PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody LogGrade logGrade) {
        Integer number = logGradeService.insert(logGrade);
        return ok(number);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改", notes = "修改")
    public ResponseMessage update(@PathVariable("id") Long id, @RequestBody LogGrade logGrade) {
        Integer number = logGradeService.update(id, logGrade);
        return ok(number);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        logGradeService.delete(id);
        return ok();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info(@PathVariable("id") Long id) {
        LogGrade logGrade = logGradeService.selectById(id);
        return ok(logGrade);
    }

    @ApiOperation(value = "查询列表", notes = "查询列表")
    @GetMapping
    public ResponseMessage list(@RequestParam(value = "pageNo", required = false) Integer pageNo,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Page<LogGrade> page = logGradeService.page(new Page<LogGrade>(pageNo, pageSize));
        return ok(page);
    }*/

}