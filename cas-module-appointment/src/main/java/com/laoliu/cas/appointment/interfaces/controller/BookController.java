package com.laoliu.cas.appointment.interfaces.controller;

import com.laoliu.cas.appointment.application.service.BookService;
import com.laoliu.cas.appointment.interfaces.dto.response.BookResultResponse;
import com.laoliu.cas.common.api.GetUserIdViaTokenApi;
import com.laoliu.cas.common.domain.entity.User;
import com.laoliu.cas.common.result.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * @author forever-king
 */
@Tag(name = "预约服务")
@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final GetUserIdViaTokenApi getUserIdViaTokenApi;

    @Operation(summary = "预定服务", description = "用户预约多个服务，传入服务ID列表")
    @PostMapping
    public CommonResult<BookResultResponse> bookService(
            @Parameter(description = "服务ID列表", required = true) @RequestParam List<Integer> serviceIds) {
        Long userId = getUserIdViaTokenApi.getUserId();
        User user = bookService.bookService(userId, serviceIds);

        BookResultResponse response = new BookResultResponse();
        response.setUsername(user.getName());
        response.setEmail(user.getEmail());
        response.setGrade(user.getGrade());
        response.setAllBookedServices(bookService.getAllBookings(userId));
        return CommonResult.success("预约成功", response);
    }

    @Operation(summary = "查看所有预约", description = "获取当前用户的所有预约记录")
    @GetMapping("/allService")
    public CommonResult<List<Map<String, Object>>> getBook() {
        Long userId = getUserIdViaTokenApi.getUserId();
        List<Map<String, Object>> bookings = bookService.getAllBookings(userId);
        if (bookings == null || bookings.isEmpty()) {
            return CommonResult.notFound("暂无预约记录");
        }
        return CommonResult.success(bookings);
    }

    @Operation(summary = "取消预约", description = "取消用户已预约的服务，传入预约ID列表")
    @PostMapping("/cancel")
    public CommonResult<Void> cancelBooking(
            @Parameter(description = "预约ID列表", required = true) @RequestParam List<Long> bookingIds) {
        Long userId = getUserIdViaTokenApi.getUserId();
        boolean success = bookService.cancelBookings(userId, bookingIds);
        if (!success) {
            return CommonResult.badRequest("取消预约失败");
        }
        return CommonResult.success("取消预约成功", null);
    }
}