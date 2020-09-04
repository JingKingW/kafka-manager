package com.xiaojukeji.kafka.manager.web.inteceptor;

import com.alibaba.fastjson.JSON;
import com.xiaojukeji.kafka.manager.common.constant.StatusCode;
import com.xiaojukeji.kafka.manager.common.entity.Result;
import com.xiaojukeji.kafka.manager.common.entity.bizenum.AccountRoleEnum;
import com.xiaojukeji.kafka.manager.common.entity.po.AccountDO;
import com.xiaojukeji.kafka.manager.common.utils.EncryptUtil;
import com.xiaojukeji.kafka.manager.dao.AccountDao;
import com.xiaojukeji.kafka.manager.service.service.LoginService;
import com.xiaojukeji.kafka.manager.web.vo.AccountVO;
import org.apache.commons.lang.StringUtils;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

/**
 * @author WangYanjing
 * @description
 * @date 2020/9/3 17:19
 */
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();
        if (principal == null) {
            return false;
        }
        String username = principal.getName();
        if (StringUtils.isEmpty(username)) {
            return false;
        }
        AccountDO accountDO = accountDao.getByName(username);
        if (accountDO == null) {
            accountDO = new AccountDO();
            accountDO.setUsername(username);
            accountDO.setPassword("abc123");
            accountDO.setRole(0);
            loginService.addNewAccount(accountDO);
        }
        AccountRoleEnum userRoleEnum = AccountRoleEnum.getUserRoleEnum(accountDO.getRole());
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(24 * 60 * 60);
        session.setAttribute("role", userRoleEnum);
        session.setAttribute("username", username);
        return true;
    }
}
