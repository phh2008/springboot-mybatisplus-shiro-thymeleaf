package com.phh.controller;


import com.phh.constants.ShiroConst;
import com.phh.entity.mem.User;
import com.phh.shiro.MyPrincipal;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * base controller
 *
 * @author phh
 * @version V1.0
 * @date 2019/5/16
 */
public class BaseController {

    /**
     * 是否已登录，否则抛出异常
     */
    public void requiredLogin() {
        if (!getSubject().isAuthenticated()) {
            throw new UnauthenticatedException();
        }
    }

    /**
     * shiro-获取当前登录用户的主体
     *
     * @return
     */
    public MyPrincipal getPrincipal() {
        return (MyPrincipal) SecurityUtils.getSubject().getPrincipal();
    }

    /**
     * shiro-获取会话
     *
     * @return
     */
    public Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    /**
     * shiro-subject
     *
     * @return
     */
    public Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    /**
     * 当前session登录用户
     *
     * @return
     */
    public User getUserVO() {
        return (User) SecurityUtils.getSubject().getSession().getAttribute(ShiroConst.SHIRO_SESSION_USER);
    }

}