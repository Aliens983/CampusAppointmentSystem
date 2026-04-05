package com.laoliu.system.controller;

import com.laoliu.system.api.GetUserIdViaTokenApi;
import com.laoliu.system.common.exception.enums.BookErrorCode;
import com.laoliu.system.common.result.CommonResult;
import com.laoliu.system.entity.User;
import com.laoliu.system.service.BookService;
import com.laoliu.system.utils.JWTUtils;
import com.laoliu.system.vo.response.BookResultResponse;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

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
    public CommonResult<BookResultResponse> bookService(HttpServletRequest request, @RequestParam List<Integer> serviceIds) {
        // TODO：当我token不是正确的token或者token解析失败的时候，就是放回500,而不是其他错误类型
        String token = request.getHeader("Authorization");
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Claims claims = jwtUtils.parseToken(token);
            Long userId = Long.valueOf((claims.getSubject()));
            User user = bookService.bookService(userId, serviceIds);
            BookResultResponse response = new BookResultResponse();
            response.setUsername(user.getName());
            response.setEmail(user.getEmail());
            response.setGrade(user.getGrade());
            response.setAllBookedServices(bookService.getAllBookings(userId));
            return CommonResult.success("预约成功", response);
        } catch (Exception e) {
            return CommonResult.error(BookErrorCode.BOOKING_FAILED, e.getMessage());
        }
    }

    @GetMapping("/allService")
    @Operation(summary = "查看所有预约")
    public CommonResult<List<Map<String, Object>>> getBook(HttpServletRequest request) {
        try {
            Long userId = getUserIdViaTokenApi.getUserId(request);
            return CommonResult.success(bookService.getAllBookings(userId));
        } catch (Exception e) {
            return CommonResult.internalServerError("获取预约失败：" + e.getMessage());
        }
    }

    @PostMapping("/cancel")
    @Operation(summary = "取消预约")
    public CommonResult<Void> cancelBooking(HttpServletRequest request, @RequestParam List<Long> bookingIds) {
        try {
            Long userId = getUserIdViaTokenApi.getUserId(request);
            boolean success = bookService.cancelBookings(userId, bookingIds);
            if (success) {
                return CommonResult.success("取消预约成功", null);
            } else {
                return CommonResult.error(BookErrorCode.BOOKING_CANCEL_FAILED);
            }
        } catch (Exception e) {
            return CommonResult.internalServerError("取消预约失败：" + e.getMessage());
        }
    }
}
