package com.laoliu.system.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

/**
 * @author 25516
 */
@Component
@Slf4j
public class JWTUtils {

    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 生成JWT Token
     * @param userId 用户ID，作为用户标识
     * @return JWT Token字符串
     */
    public String generateToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        // 将Base64编码的密钥字符串转换为字节数组并生成密钥
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);

        JwtBuilder jwtBuilder = Jwts.builder()
                .subject(String.valueOf(userId))
                // 在payload中添加用户ID信息
                .claim("userId", userId)
                .issuedAt(now)
                .expiration(expiryDate)
                // 使用HS512算法签名
                .signWith(secretKey, SignatureAlgorithm.HS512);

        String jwtToken = jwtBuilder.compact();
        log.info("Successfully generated JWT token for userId: {}", userId);
        log.info("JWT Token: {}", jwtToken);
        return jwtToken;
    }

    /**
     * 解析JWT Token
     * @param token JWT Token字符串
     * @return Claims对象，包含Token中的信息
     */
    public Claims parseToken(String token) {
        try {
            // 将Base64编码的密钥字符串转换为字节数组并生成密钥
            byte[] keyBytes = Base64.getDecoder().decode(secret);
            SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);

            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build().parseSignedClaims(token);

            return claimsJws.getPayload();
        } catch (ExpiredJwtException e) {
            log.error("JWT token has expired: {}", e.getMessage());
            throw new RuntimeException("Token已过期");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
            throw new RuntimeException("不支持的Token类型");
        } catch (MalformedJwtException e) {
            log.error("Malformed JWT token: {}", e.getMessage());
            throw new RuntimeException("Token格式错误");
        } catch (IllegalArgumentException e) {
            log.error("JWT token is empty: {}", e.getMessage());
            throw new RuntimeException("Token不能为空");
        } catch (Exception e) {
            log.error("Error parsing JWT token: {}", e.getMessage());
            throw new RuntimeException("Token解析失败");
        }
    }
}
