package com.phh.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.phh.shiro.AdminAuthenticationFilter;
import com.phh.shiro.AdminAuthorizingRealm;
import com.phh.shiro.MyCredentialsMatcher;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * <p> shiro 配置</p>
 *
 * @author phh
 * @version V1.0
 * @date 2018/1/17
 */
@Configuration
public class ShiroConfig {

    /**
     * thymeleaf shiro标签
     *
     * @return
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    @Bean
    public AdminAuthenticationFilter authFilter() {
        AdminAuthenticationFilter filter = new AdminAuthenticationFilter();
        //表单中账号的input名称
        filter.setUsernameParam("username");
        //表单中密码的input名称
        filter.setPasswordParam("password");
        //表单中记住我的input名称
        filter.setRememberMeParam("rememberMe");
        //loginurl：用户登陆地址，此地址是可以http访问的url地址
        filter.setLoginUrl("/login");
        //成功页面
        filter.setSuccessUrl("/");
        return filter;
    }

    /**
     * 这个解决spring把tokenAuthFilter注册为ApplicationFilterChain的问题，
     * tokenAuthFilter只注册到shiroFilter中
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean disableTokenFilter(AdminAuthenticationFilter authFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(authFilter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public AdminAuthorizingRealm adminAuthorizingRealm(CredentialsMatcher credentialsMatcher) {
        AdminAuthorizingRealm adminAuthorizingRealm = new AdminAuthorizingRealm();
        adminAuthorizingRealm.setCachingEnabled(true);
        adminAuthorizingRealm.setAuthorizationCacheName("admin:auth:cache");
        adminAuthorizingRealm.setCredentialsMatcher(credentialsMatcher);
        return adminAuthorizingRealm;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager, Map<String, Filter> filters) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setFilters(filters);
        //拦截器.
        Map<String, String> filterChainDefinitionMap = shiroFilterFactoryBean.getFilterChainDefinitionMap();
        filterChainDefinitionMap.put("/error", "anon");
        filterChainDefinitionMap.put("/404", "anon");
        filterChainDefinitionMap.put("/403", "anon");
        filterChainDefinitionMap.put("/502", "anon");
        filterChainDefinitionMap.put("/test", "anon");
        filterChainDefinitionMap.put("/test2", "anon");
        filterChainDefinitionMap.put("/test3", "anon");
        filterChainDefinitionMap.put("/unauthorized", "anon");
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/error/**", "anon");
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/**", "authFilter");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        shiroFilterFactoryBean.setLoginUrl("/login");
        //指定没有权限操作时跳转页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager(SessionManager sessionManager,
                                           CacheManager cacheManager,
                                           SubjectFactory subjectFactory,
                                           RememberMeManager rememberMeManager,
                                           AdminAuthorizingRealm adminAuthorizingRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //缓存管理器
        securityManager.setCacheManager(cacheManager);
        //会话管理器
        securityManager.setSessionManager(sessionManager);
        //subject工厂
        securityManager.setSubjectFactory(subjectFactory);
        //记住我管理器
        securityManager.setRememberMeManager(rememberMeManager);
        SecurityUtils.setSecurityManager(securityManager);
        //realm
        securityManager.setRealm(adminAuthorizingRealm);
        return securityManager;
    }

    @Bean
    public SubjectFactory subjectFactory() {
        return new DefaultWebSubjectFactory();
    }

    /**
     * 缓存管理
     *
     * @return
     */
    @Bean
    public CacheManager cacheManager() {
        //内存缓存实现
        return new MemoryConstrainedCacheManager();
    }

    /**
     * 会话cookie设置
     *
     * @return
     */
    @Bean
    public Cookie sessionCookie() {
        SimpleCookie cookie = new SimpleCookie();
        //cookie 名称设置
        cookie.setName("JSESSIONID");
        //<!-- cookie位置 -->
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        //<!-- maxAge为-1表示浏览器关闭时失效此Cookie -->
        cookie.setMaxAge(-1);
        //域名设置
        //cookie.setDomain("127.0.0.1")
        return cookie;
    }

    /**
     * 记住我cookie
     *
     * @return
     */
    @Bean
    public Cookie rememberMeCookie() {
        SimpleCookie cookie = new SimpleCookie();
        //<!-- rememberMe是cookie的名字 -->
        cookie.setName("rememberMe");
        //<!-- 记住我cookie生效时间30天 -->
        cookie.setMaxAge(2592000);
        return cookie;
    }

    /**
     * 会话管理
     *
     * @param cacheManager
     * @param sessionCookie
     * @return
     */
    @Bean
    public DefaultWebSessionManager sessionManager(CacheManager cacheManager, Cookie sessionCookie) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //<!--session的失效时长，单位毫秒-- >
        sessionManager.setGlobalSessionTimeout(1800000);
        //<!--相隔多久检查一次session的有效性-- >
        sessionManager.setSessionValidationInterval(600000);
        //<!--是否开启扫描-- >
        sessionManager.setSessionValidationSchedulerEnabled(true);
        //<!--删除失效的session-- >
        sessionManager.setDeleteInvalidSessions(true);
        //缓存
        sessionManager.setCacheManager(cacheManager);
        //cookie设置
        sessionManager.setSessionIdCookie(sessionCookie);
        return sessionManager;
    }

    /**
     * 记住我
     *
     * @param rememberMeCookie
     * @return
     */
    @Bean
    public RememberMeManager rememberMeManager(Cookie rememberMeCookie) {
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        rememberMeManager.setCookie(rememberMeCookie);
        return rememberMeManager;
    }

    /**
     * 密码匹配
     *
     * @return
     */
    @Bean
    public MyCredentialsMatcher credentialsMatcher() {
        return new MyCredentialsMatcher();
    }


    //以下是支持注解权限配置--------------------------------------------

    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
