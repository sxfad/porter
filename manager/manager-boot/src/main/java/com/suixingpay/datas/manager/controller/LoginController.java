/**
 *
 */
package com.suixingpay.datas.manager.controller;

import com.suixingpay.datas.manager.core.dto.LoginUserToken;
import com.suixingpay.datas.manager.core.entity.CUser;
import com.suixingpay.datas.manager.core.init.MenuUtils;
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
    public ResponseMessage login(@RequestParam(required = true) String loginName,
                                 @RequestParam(required = true) String passwd) throws Exception {
        LoginUserToken loginUserToken = new LoginUserToken();
        CUser cuser = cuserService.selectByNameAndpasswd(loginName, passwd);
        if (cuser == null) {
            return ResponseMessage.error("Login error", ExceptionCode.EXCEPTION_LOGIN);
        } else if (cuser.getState() == 0) {
            return ResponseMessage.error("对不起,您被禁止登陆。", ExceptionCode.EXCEPTION_LOGIN);
        } else {
            loginUserToken.setUserId(cuser.getId());
            loginUserToken.setLoginName(cuser.getLoginname());
            loginUserToken.setPasswd(cuser.getLoginpw());
            loginUserToken.setRoleCode(cuser.getRoleCode());
            String token = TokenUtil.sign(loginUserToken);
            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
//            map.put("CMenu", MenuUtils.ROLE_MENU.get(loginUserToken.getRoleCode()));
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
        map.put("roleCode", loginUserToken.getRoleCode());
        map.put("CMenu", MenuUtils.ROLE_MENU.get(loginUserToken.getRoleCode()));
        return ResponseMessage.ok(map);
    }

}
