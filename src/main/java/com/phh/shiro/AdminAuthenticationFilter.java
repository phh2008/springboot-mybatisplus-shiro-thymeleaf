package com.phh.shiro;

import com.phh.constants.MsgConst;
import com.phh.constants.ShiroConst;
import com.phh.utils.WebUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证过滤器
 *
 * @author phh
 * @version V1.0
 * @date 2019/5/16
 */
public class AdminAuthenticationFilter extends FormAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(AdminAuthenticationFilter.class);


    /**
     * 即是否允许访问，返回true表示允许
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (this.isLoginRequest(request, response)) {
            if (this.isLoginSubmission(request, response)) {
                //本次用户登陆账号
                String account = this.getUsername(request);
                //当前用户
                Subject subject = this.getSubject(request, response);
                //之前登陆的用户
                MyPrincipal user = (MyPrincipal) subject.getPrincipal();
                //如果两次登陆的用户不一样，则先退出之前登陆的用户
                if (account != null && user != null && !account.equals(user.getUserName())) {
                    subject.logout();
                }
            }
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }

    /**
     * 表示访问拒绝时是否自己处理，如果
     * 返回true表示自己不处理且继续拦截器链执行，
     * 返回false表示自己已经处理了（比如重定向到另一个页面）
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (this.isLoginRequest(request, response)) {
            if (this.isLoginSubmission(request, response)) {
                if (log.isTraceEnabled()) {
                    log.trace("Login submission detected.  Attempting to execute login.");
                }
                //执行登录
                return this.executeLogin(request, response);
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("Login page view.");
                }
                return true;
            }
        } else {
            if (log.isTraceEnabled()) {
                log.trace("Attempting to access a path which requires authentication.  Forwarding to the Authentication url [" + this.getLoginUrl() + "]");
            }
            if (WebUtil.isAjax((HttpServletRequest) request)) {
                //ajax请求
                Map<String, Object> obj = new HashMap<>(2);
                obj.put("code", MsgConst.NOT_LOGIN);
                obj.put("msg", "未登录");
                WebUtil.writeJson(response, obj);
            } else {
                saveRequestAndRedirectToLogin(request, response);
            }
            return false;
        }
    }

    /**
     * 登录成功处理
     *
     * @param token
     * @param subject
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        if (WebUtil.isAjax((HttpServletRequest) request)) {
            //ajax请求
            Map<String, Object> obj = new HashMap<>(2);
            obj.put("code", MsgConst.SUCCESS);
            obj.put("msg", "SUCCESS");
            WebUtil.writeJson(response, obj);
        } else {
            this.issueSuccessRedirect(request, response);
        }
        return false;
    }

    @Override
    protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
        WebUtils.getAndClearSavedRequest(request);
        String successUrl = getSuccessUrl();
        WebUtils.redirectToSavedRequest(request, response, successUrl);
    }


    /**
     * 登录失败处理
     *
     * @param token
     * @param e
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        log.error("Authentication component", e);
        // 失效 session
        SecurityUtils.getSubject().getSession().removeAttribute(ShiroConst.SHIRO_SESSION_USER);
        String message = e.getClass().getSimpleName();
        String errorMsg = "";
        if ("IncorrectCredentialsException".equals(message)) {
            errorMsg = "账号或密码错误";
        } else if ("UnknownAccountException".equals(message)) {
            errorMsg = "账号或密码错误";
        } else if ("LockedAccountException".equals(message)) {
            errorMsg = "账号被锁定";
        } else if ("AuthenticationException".equals(message)) {
            errorMsg = "登录失败，系统错误";
        } else if ("CustAuthenticationException".equals(message)) {
            errorMsg = e.getMessage();
        } else {
            errorMsg = "登录失败，未知错误";
        }
        if (WebUtil.isAjax((HttpServletRequest) request)) {
            //ajax请求
            Map<String, Object> obj = new HashMap<>();
            obj.put("errCode", MsgConst.LOGIN_FAIL);
            obj.put("errMsg", errorMsg);
            WebUtil.writeJson(response, obj);
        } else {
            request.setAttribute(this.getFailureKeyAttribute(), errorMsg);
        }
        return true;
    }

}
