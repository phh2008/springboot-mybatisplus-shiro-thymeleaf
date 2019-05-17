package com.phh.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * 密码匹配器
 * Created by phh on 2017/8/25.
 */
public class MyCredentialsMatcher implements org.apache.shiro.authc.credential.CredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        //请求密码
        Object reqPwd = authenticationToken.getCredentials();
        //数据库密码
        Object dbPwd = authenticationInfo.getCredentials();
        if (reqPwd == null || dbPwd == null) {
            return false;
        }
        if (reqPwd instanceof char[]) {
            char[] chs = (char[]) reqPwd;
            reqPwd = String.valueOf(chs);
        }
        //TODO 密码匹配 e.g  Objects.equals(reqPwd,dbPwd)
        return true;
    }

}
