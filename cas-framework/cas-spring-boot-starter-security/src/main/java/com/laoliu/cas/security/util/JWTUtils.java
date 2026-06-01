package com.laoliu.cas.security.util;

import com.laoliu.cas.common.security.LoginUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;

/**
 * @author forever-king
 */
@Slf4j
@Component
public class JWTUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        byte[] keyBytes = Base64.getDecoder().decode(secret);
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("userId", userId)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateToken(LoginUser loginUser) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        byte[] keyBytes = Base64.getDecoder().decode(secret);
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .subject(String.valueOf(loginUser.getId()))
                .claim("userId", loginUser.getId())
                .claim("name", loginUser.getName())
                .claim("role", loginUser.getRole())
                .claim("email", loginUser.getEmail())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims parseToken(String token) {
        try {
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

    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return Long.parseLong(claims.get("userId").toString());
    }

    public LoginUser getLoginUserFromToken(String token) {
        try {
            Claims claims = parseToken(token);

            LoginUser loginUser = new LoginUser();
            loginUser.setId(Long.parseLong(claims.get("userId").toString()));

            Object nameObj = claims.get("name");
            if (nameObj != null) {
                loginUser.setName(nameObj.toString());
            }

            Object roleObj = claims.get("role");
            if (roleObj != null) {
                if (roleObj instanceof Number) {
                    loginUser.setRole(((Number) roleObj).intValue());
                } else {
                    loginUser.setRole(Integer.parseInt(roleObj.toString()));
                }
            }

            Object emailObj = claims.get("email");
            if (emailObj != null) {
                loginUser.setEmail(emailObj.toString());
            }

            return loginUser;
        } catch (Exception e) {
            log.error("Error extracting user from token: {}", e.getMessage());
            return null;
        }
    }
}