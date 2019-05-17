package com.phh.shiro;

import com.phh.constants.ShiroConst;
import com.phh.entity.mem.User;
import com.phh.service.mem.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 认证授权预处理器
 * Created by phh on 2017/8/25 0025.
 */
public class AdminAuthorizingRealm extends AuthorizingRealm {

    private static final Logger log = LoggerFactory.getLogger(AdminAuthorizingRealm.class);

    /**
     * 权限缓存key前辍
     */
    private static final String AUTH_CACHE_KEY_PREFIX = "auth:";

    @Autowired
    private IUserService userService;

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    /**
     * 授权处理
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //获取身份信息
        MyPrincipal ufaPrincipal = (MyPrincipal) principals.getPrimaryPrincipal();
        log.info(">>>>授权处理doGetAuthorizationInfo running principal:" + ufaPrincipal);
        //TODO 获取角色
        List<String> roles = new ArrayList<>();
        //TODO 获取权限
        List<String> permissions = new ArrayList<>();

        //返回授权信息
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //权限
        simpleAuthorizationInfo.addStringPermissions(permissions);
        //角色
        simpleAuthorizationInfo.addRoles(roles);
        return simpleAuthorizationInfo;
    }

    /**
     * 认证处理
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String principal = String.valueOf(authenticationToken.getPrincipal());
        log.info(">>>>认证处理doGetAuthenticationInfo running principal:" + principal);
        //1:根据principal从数据查询用户信息，不存在返回null
        //TODO
        User userVO = userService.getByUsername(principal);
        if (userVO == null) {
            //用户不存在返回NULL shiro自动处理
            return null;
        }
        //2:返回SimpleAuthenticationInfo
        MyPrincipal ufaPrincipal = new MyPrincipal(userVO);
        //保存当前的userVO对象到session
        SecurityUtils.getSubject().getSession().setAttribute(ShiroConst.SHIRO_SESSION_USER, userVO);
        return new SimpleAuthenticationInfo(ufaPrincipal, userVO.getPassword(), getName());
    }

    /**
     * 缓存key生成方式
     *
     * @param principals
     * @return
     */
    @Override
    protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
        return AUTH_CACHE_KEY_PREFIX + ((MyPrincipal) principals.getPrimaryPrincipal()).getUserName();
    }

    /**
     * 清除权限缓存
     *
     * @param principals
     */
    @Override
    public void doClearCache(PrincipalCollection principals) {
        log.info("清除权限缓存 doClearCache principal:" + principals.getPrimaryPrincipal());
        super.doClearCache(principals);
        this.clearCachedAuthorizationInfo(principals);
    }

    /**
     * 清空所有授权缓存
     */
    public void clearAllCache() {
        Cache<Object, AuthorizationInfo> cache = this.getAuthorizationCache();
        if (cache != null) {
            cache.clear();
        }
    }

}
