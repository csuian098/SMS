package com.yb.config;

import com.yb.utils.JwtShiroUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtShiroRealm extends AuthorizingRealm {

    @Autowired
    private JwtShiroUtil jwtUtils;

    /**
     * 支持JwtShiroToken类型
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtShiroToken;  // 只处理JwtShiroToken
    }

    /**
     * 授权逻辑(获取用户角色/权限)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 从 principals 中获取用户名
        String username = jwtUtils.getUsernameFromToken(principals.toString());
        // 查询用户角色(实际项目中从数据库获取)
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //info.addRoles(userService.getRoles(username));  // 假设返回角色列表
        //info.addStringPermissions(userService.getPermissions(username));  // 假设返回权限列表
        return info;
    }

    /**
     * 认证逻辑(验证用户身份)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String jwtToken = (String) token.getCredentials();
        // 1. 验证Token有效性
        if (!jwtUtils.validateToken(jwtToken)) {
            throw new AuthenticationException("Token无效或已过期");
        }
        // 2. 从Token中获取用户名
        String username = jwtUtils.getUsernameFromToken(jwtToken);
        if (username == null) {
            throw new AuthenticationException("Token中未包含用户名");
        }
        // 3. 查询用户是否存在(实际项目中从数据库验证)
//        if (!userService.exists(username)) {  // 假设exists方法判断用户是否存在
//            throw new UnknownAccountException("用户不存在");
//        }
        // 4. 构建认证信息(Shiro会自动处理后续逻辑)
        return new SimpleAuthenticationInfo(jwtToken, jwtToken, getName());
    }
}