package com.yb.interceptor;

import com.yb.annotation.IgnoreAuth;
import com.yb.config.JwtShiroToken;
import com.yb.entity.TokenEntity;
import com.yb.service.TokenService;
import com.yb.utils.SpringContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT身份验证过滤器，继承自Shiro的基本HTTP认证过滤器。
 * 用于拦截请求并验证JWT Token的有效性，处理未认证请求及跨域问题。
 */
public class JwtShiroFilter extends AuthenticatingFilter {

    // Token请求头名称(如: Authorization)
    private static final String TOKEN_HEADER = "token";

    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        if (null == requestMappingHandlerMapping) {
            requestMappingHandlerMapping = SpringContextUtils.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        }
        return requestMappingHandlerMapping;
    }

    // 创建自定义token
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String token = httpRequest.getHeader(TOKEN_HEADER);
        return new JwtShiroToken(token);
    }

    // 拦截请求
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (isPublicPath(httpRequest.getRequestURI()) || isIgnoreAuthRequest(httpRequest)) {
            // 如果标记了 @IgnoreAuth，直接放行
            return true;
        }
        System.out.println(httpRequest.getRequestURI());
        String token = httpRequest.getHeader(TOKEN_HEADER);
        if (StringUtils.isNotBlank(token)) {
            TokenEntity tokenEntity = SpringContextUtils.getBean("tokenService", TokenService.class).getTokenEntity(token);
            if (tokenEntity != null) {
                setSessionAttributes(httpRequest, tokenEntity);
            }
        }
        return executeLogin(request, response);
    }

    // 处理登录失败
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e,
                                     ServletRequest request, ServletResponse response) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        try {
            e.printStackTrace();
            httpResponse.getWriter().write("{\"code\":401,\"msg\":\"请检查是否授予权限，请求将跳转登录页面！\"}");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return false;
    }

    // 处理跨域请求
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));

        // 处理OPTIONS请求
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }

        return super.preHandle(request, response);
    }

    private boolean isPublicPath(String uri) {
        return uri.endsWith("/login") || uri.endsWith("/logout") || uri.endsWith("/logout");
    }

    private boolean isIgnoreAuthRequest(HttpServletRequest request) {
        try {
            // 在需要使用的地方调用，例如 JwtShiroFilter 中
            HandlerExecutionChain handlerExecutionChain = getRequestMappingHandlerMapping().getHandler(request);
            if (handlerExecutionChain != null) {
                Object handler = handlerExecutionChain.getHandler();
                if (handler instanceof HandlerMethod) {
                    HandlerMethod handlerMethod = (HandlerMethod) handler;
                    return handlerMethod.hasMethodAnnotation(IgnoreAuth.class);
                }
            } else {
                //logger.warn("Handler not found for request URI: " + request.getRequestURI());
            }
        } catch (Exception e) {
            //logger.error("检查@IgnoreAuth注解时出错", e);
        }
        return false;
    }

    private void setSessionAttributes(HttpServletRequest request, TokenEntity tokenEntity) {
        request.getSession().setAttribute("userId", tokenEntity.getUserid());
        request.getSession().setAttribute("role", tokenEntity.getRole());
        request.getSession().setAttribute("tableName", tokenEntity.getTablename());
        request.getSession().setAttribute("username", tokenEntity.getUsername().split("_")[0]);
    }
}