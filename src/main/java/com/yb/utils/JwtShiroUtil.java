package com.yb.utils;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtShiroUtil {

    // 从配置文件读取JWT密钥和过期时间
    private String secret = "springboot32e20828n";  // 密钥 (如: abc123456)
    private long expire = 1 * 60 * 60 * 1000;    // 过期时间(毫秒, 1小时)

    /**
     * 生成JWT Token
     *
     * @param username 用户名(作为token的subject)
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expire);

        // 自定义claims(可选, 可存储用户角色等信息)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims)  // 自定义信息
                .setSubject(username)  // 主题(用户名)
                .setIssuedAt(now)  // 签发时间
                .setExpiration(expireDate)  // 过期时间
                .signWith(SignatureAlgorithm.HS256, secret)  // 签名算法+密钥
                .compact();
    }

    /**
     * 从Token中获取用户名
     */
    public String getUsernameFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            log.error("解析Token失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 验证Token有效性
     *
     * @return true: 有效; false: 无效(过期/签名错误等)
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token已过期");
        } catch (UnsupportedJwtException e) {
            log.error("不支持的Token格式");
        } catch (MalformedJwtException e) {
            log.error("Token格式错误");
        } catch (SignatureException e) {
            log.error("Token签名错误");
        } catch (IllegalArgumentException e) {
            log.error("Token参数为空");
        }
        return false;
    }
}