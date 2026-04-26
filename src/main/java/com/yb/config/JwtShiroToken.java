package com.yb.config;

import org.apache.shiro.authc.AuthenticationToken;

public class JwtShiroToken implements AuthenticationToken {

    private String token;  // JWT字符串

    public JwtShiroToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;  // 主体(这里返回token)
    }

    @Override
    public Object getCredentials() {
        return token;  // 凭证(这里返回token)
    }
}