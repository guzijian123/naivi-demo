package com.gzj.demo.utils;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * jwt 工具类
 * @author 20412
 */
@Slf4j
public class JwtUtils {

    /** 过期时间设置 365天*/
    private final static long tokenExpiration = 365 * 24 * 60 * 60 * 1000;
    /** jwt密钥*/
    private final static String tokenSecret = "guzijian";

    /** 创建token  */
    public static String createToken(String username, String userId) {
        String token = Jwts.builder()
                // 说明
                .setSubject("AUTH-USER")
                // 过期时间
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                // 加入认证信息
                .claim("userId",userId)
                .claim("username",username)
                // 加密方式 jwt 密钥方便解密
                .signWith(SignatureAlgorithm.HS256, tokenSecret)
                // 压缩方式
                .compressWith(CompressionCodecs.GZIP)
                // 构建契约
                .compact();
        return token;
    }

    /** 通过token获取user id  */
    public static String getUserId(String token){
        Claims claims = null;
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(tokenSecret)
                    .parseClaimsJws(token);
            claims = claimsJws.getBody();
        } catch (Exception e) {
            log.info("token:{}，反解析报错：{}",token,e.getMessage());
            return null;
        }
        return (String)claims.get("userId");
    }

    /** 获取用户id*/
    public static String getUserId(HttpServletRequest request){
        String token = request.getHeader("Authority");
        Claims claims = null;
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(tokenSecret)
                    .parseClaimsJws(token);
            claims = claimsJws.getBody();
        } catch (Exception e) {
            throw new RuntimeException("解析失败" + e.getMessage());
        }
        return (String)claims.get("userId");
    }

    public static String getUserName(String token){
        Claims claims = null;
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(tokenSecret)
                    .parseClaimsJws(token);
            claims = claimsJws.getBody();
        } catch (Exception e) {
            throw new RuntimeException("token解析失败" + e.getMessage());
        }
        return (String)claims.get("username");
    }

    public static String getUserName(HttpServletRequest request){
        String token = request.getHeader("Authority");
        Claims claims = null;
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(tokenSecret)
                    .parseClaimsJws(token);
            claims = claimsJws.getBody();
        } catch (Exception e) {
            throw new RuntimeException("token解析失败" + e.getMessage());
        }
        return (String)claims.get("username");
    }

    public String getUserIdByRequest(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        Claims claims = null;
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(tokenSecret)
                    .parseClaimsJws(authorization);
            claims = claimsJws.getBody();
        } catch (Exception e) {
            throw new RuntimeException("token解析失败" + e.getMessage());
        }
        return (String)claims.get("userId");
    }

//    public static void main(String[] args) {
//        String guzijian = createToken(new UserCustom(new User("1", "204126329@qq.com", "guzijian"), Collections.emptyList()));
//        System.out.println(guzijian);
//    }
}
