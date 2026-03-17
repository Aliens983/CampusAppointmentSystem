package com.laoliu.system.controller;

import com.laoliu.system.entity.User;
import com.laoliu.system.service.BookService;
import com.laoliu.system.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 25516
 */
@RestController
@RequestMapping("/book")
public class BookController {


    private final BookService bookService;
    private final JWTUtils jwtUtils;

    public BookController(BookService bookService, JWTUtils jwtUtils) {
        this.bookService = bookService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping
    @Operation(summary = "预定服务")
    public ResponseEntity<Map<String, Object>> bookService(HttpServletRequest request, @RequestParam Integer serviceId) {

        Map<String, Object> result = new HashMap<>();
        String token = request.getHeader("Authorization");
        try {
            // 移除 "Bearer " 前缀
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Claims claims = jwtUtils.parseToken(token);
            result.put("success", true);


            // 获取用户ID
            Long userId = Long.valueOf((claims.getSubject()));

            User user = bookService.bookService(userId, serviceId);
            result.put("user", user);
            return ResponseEntity.ok(result);


        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            result.put("error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
