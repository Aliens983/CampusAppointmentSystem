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
import java.util.List;
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
    public ResponseEntity<Map<String, Object>> bookService(HttpServletRequest request, @RequestParam List<Integer> serviceId) {

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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
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

    @PostMapping("/cancel")
    @Operation(summary = "取消预约")
    public ResponseEntity<Map<String, Object>> cancelBook(HttpServletRequest request, @RequestParam List<Integer> serviceIds) {

        Map<String,Object> result = new HashMap<>();
        try {
            Long userId = getUserIdViaTokenApi.getUserId(request);
            boolean status = bookService.cancelBook(userId, serviceIds);
            if (!status) {
                result.put("success", false);
                result.put("message", "取消预约失败");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
            }
            result.put("success", true);
            result.put("message", "取消预约成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @PutMapping
    @Operation(summary = "管理员修改预约状态")
    public ResponseEntity<Map<String, Object>> updateBookStatus(HttpServletRequest request, @RequestParam Integer serviceId,@RequestParam String status) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long userId = getUserIdViaTokenApi.getUserId(request);
            boolean done = bookService.changeStatus(userId, serviceId , status);
            if (!done) {
                result.put("success", false);
                result.put("message", "修改预约状态失败");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
            }
            result.put("success", true);
            result.put("message", "修改预约状态成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
        //TODO 这里还要判断一下用户是否是管理员, 只有管理员才有权限修改预约状态，而且还要判断一下预约是否存在
    }

}
