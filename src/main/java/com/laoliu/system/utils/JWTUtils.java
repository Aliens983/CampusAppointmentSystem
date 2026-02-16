package com.laoliu.system.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

/**
 * @author 25516
 */
@Component
@Slf4j
public class JWTUtils {

    private static final String SECRET_STRING = "diaozhatian1234567890123456789012";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));


    public String generateJwt() {
        //创建Jwt对象
        JwtBuilder jwtBuilder = Jwts.builder();
        //创建对应的三个部分
        String jwtToken= jwtBuilder
                //设置头信息header
                .setHeaderParam("type", "JWT")
                .setHeaderParam("alg", "HS256")
                //添加载荷payload
                .claim("username", "laoliu")
                .claim("role", "admin")
                .subject("jwt_test")
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .id(UUID.randomUUID().toString())
                //设置签名signature
                .signWith(KEY)
                .compact();
        log.info("Successfully Login: jwtToken is: {}", jwtToken);
        return jwtToken;
    }

    public void parseJwt() {
        String token = "eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJ1c2VybmFtZSI6Imxhb2xpdSIsInJvbGUiOiJhZG1pbiIsInN1YiI6Imp3dF90ZXN0IiwiZXhwIjoxNzcxMjk0MzQyLCJqdGkiOiI3MzQ1MjVkOS1kN2E5LTQ5YmItYTFmMi00OTY0YTZjNjc0ZGMifQ.p5rzz4y7zsGcUJWnYTZqpdPbR0iQcEzX7caDIcoHwKA";
        // 解析
        Jws<Claims> claimsJws = Jwts.parser()
                // 使用相同的密钥
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token);

        Claims claims = claimsJws.getBody();

        // 输出解析结果
        log.info("解析结果:");
        log.info("用户名: {}", claims.get("username"));
        log.info("角色: {}", claims.get("role"));
        log.info("主题: {}", claims.getSubject());
        log.info("过期时间: {}", claims.getExpiration());
        log.info("ID: {}", claims.getId());
        log.info("头部: {}", claimsJws.getHeader());
        log.info("载荷: {}", claims);
        log.info("签名: {}", claimsJws.getDigest());
        
    }
}
