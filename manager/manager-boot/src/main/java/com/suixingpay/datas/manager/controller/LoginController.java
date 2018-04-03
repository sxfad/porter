/**
 *
 */
package com.suixingpay.datas.manager.controller;

import com.suixingpay.datas.manager.core.dto.LoginUserToken;
import com.suixingpay.datas.manager.core.entity.CUser;
import com.suixingpay.datas.manager.exception.ExceptionCode;
import com.suixingpay.datas.manager.service.CUserService;
import com.suixingpay.datas.manager.web.message.ResponseMessage;
import com.suixingpay.datas.manager.web.tl.WebToeknContext;
import com.suixingpay.datas.manager.web.token.TokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 */
@RestController
@Api(description = "登陆管理")
@RequestMapping("/manager/")
public class LoginController {

    private Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    public CUserService cuserService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "用户登录", notes = "用户登录")
    public ResponseMessage login(@RequestParam(required = true) String LoginName,
            @RequestParam(required = true) String passwd) throws Exception {
        LoginUserToken loginUserToken = new LoginUserToken();
        CUser cuser = cuserService.selectByNameAndpasswd(LoginName, passwd);
        if (cuser == null) {
            return ResponseMessage.error("Login error", ExceptionCode.EXCEPTION_LOGIN);
        } else {
            loginUserToken.setUserId(cuser.getId());
            loginUserToken.setLoginName(cuser.getLoginname());
            loginUserToken.setPasswd(cuser.getLoginpw());
            String token = TokenUtil.sign(loginUserToken);
            Map<String, String> map = new HashMap<>();
            map.put("token", token);
            log.info("token=[{}]", token);
            return ResponseMessage.ok(map);
        }
    }

    @RequestMapping(value = "/getuserinfo", method = RequestMethod.GET)
    @ApiOperation(value = "当前登录用户信息", notes = "当前登录用户信息")
    public ResponseMessage getCurrentUserInfo() throws Exception {
        LoginUserToken loginUserToken = WebToeknContext.getToken(LoginUserToken.class);
        Map<String, Object> map = new HashMap<>();
        CUser cuser = cuserService.selectById(loginUserToken.getUserId());
        map.put("userId", loginUserToken.getUserId());
        map.put("loginName", loginUserToken.getLoginName());
        map.put("nickName", cuser.getNickname());
        map.put("passwd", loginUserToken.getPasswd());
        return ResponseMessage.ok(map);
    }

}
