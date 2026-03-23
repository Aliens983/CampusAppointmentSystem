package com.laoliu.system.controller;

import com.laoliu.system.api.GetUserIdViaTokenApi;
import com.laoliu.system.entity.User;
import com.laoliu.system.service.BookService;
import com.laoliu.system.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final GetUserIdViaTokenApi getUserIdViaTokenApi;

    public BookController(BookService bookService, JWTUtils jwtUtils, GetUserIdViaTokenApi getUserIdViaTokenApi) {
        this.bookService = bookService;
        this.jwtUtils = jwtUtils;
        this.getUserIdViaTokenApi = getUserIdViaTokenApi;
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

    @GetMapping("/allService")
    @Operation(summary = "查看所有预约")
    public ResponseEntity<Map<String, Object>> getBook(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long userId = getUserIdViaTokenApi.getUserId(request);
            result.put("success", true);
            result.put("userId", userId);
            result.put("bookings", bookService.getAllBookings(userId));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }

    }

}
