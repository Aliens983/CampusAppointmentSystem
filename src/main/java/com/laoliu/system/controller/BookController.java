package com.laoliu.system.controller;

import com.laoliu.system.api.GetUserIdViaTokenApi;
import com.laoliu.system.entity.User;
import com.laoliu.system.service.BookService;
import com.laoliu.system.utils.JWTUtils;
import com.laoliu.system.vo.response.BookResultResponse;
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
    public ResponseEntity<Map<String, Object>> bookService(HttpServletRequest request, @RequestParam List<Integer> serviceIds) {
        Map<String, Object> result = new HashMap<>();
        String token = request.getHeader("Authorization");
        try {
            // 移除 "Bearer " 前缀
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Claims claims = jwtUtils.parseToken(token);


            // 获取用户ID
            Long userId = Long.valueOf((claims.getSubject()));

            User user = bookService.bookService(userId, serviceIds);
            BookResultResponse response = new BookResultResponse();
            response.setUsername(user.getName());
            response.setEmail(user.getEmail());
            response.setGrade(user.getGrade());
            response.setAllBookedServices(bookService.getAllBookings(userId));
            result.put("message", "预约成功");
            result.put("success", true);
            result.put("result", response);
//            result.put("result", user);
            return ResponseEntity.ok().body(result);

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
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
    public ResponseEntity<Map<String, Object>> cancelBooking(HttpServletRequest request, @RequestParam List<Long> bookingIds) {
        //TODO: 这里取消预约时,要先检查当前用户到底有没有预约到此要取消的预约服务ID,现在是取消用户未预约的服务ID也返回取消预约成功
        Map<String, Object> result = new HashMap<>();
        try {
            Long userId = getUserIdViaTokenApi.getUserId(request);
            boolean success = bookService.cancelBookings(userId,bookingIds);
            if (success) {
                result.put("success", true);
                result.put("message", "取消预约成功");
            } else {
                result.put("success", false);
                result.put("message", "取消预约失败");
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

}
