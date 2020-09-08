package com.xiaojukeji.kafka.manager.web.inteceptor;

import com.xiaojukeji.kafka.manager.common.entity.bizenum.AccountRoleEnum;
import com.xiaojukeji.kafka.manager.common.entity.po.AccountDO;
import com.xiaojukeji.kafka.manager.dao.AccountDao;
import com.xiaojukeji.kafka.manager.service.service.LoginService;
import org.apache.commons.lang.StringUtils;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

    public final static String SESSION_KEY = AbstractCasFilter.CONST_CAS_ASSERTION;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        // 判断是否已有该用户登录的session
        if (session.getAttribute(SESSION_KEY) != null) {
            if (session.getAttribute("username") != null) {
                return true;
            } else {
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
                session.setAttribute("username", username);
                session.setAttribute("role", userRoleEnum);
                Cookie userCookie = new Cookie("username",username);
                Cookie roleCookie = new Cookie("role",userRoleEnum.getRole().toString());
                response.addCookie(userCookie);
                response.addCookie(roleCookie);
                return true;
            }
        }
        // 跳转到登录页
        String url = "/login";
        response.sendRedirect(url);
        return false;
    }
}
