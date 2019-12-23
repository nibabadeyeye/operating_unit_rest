package com.gpdi.operatingunit.config;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @Description:
 * @Author: Lxq
 * @Date: 2019/9/6 11:49
 */
public class JwtToken implements AuthenticationToken{

    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
