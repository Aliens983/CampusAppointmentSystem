package com.laoliu.cas.appointment.interfaces.controller.app;

import com.laoliu.cas.appointment.application.service.BookService;
import com.laoliu.cas.appointment.interfaces.dto.response.BookingDTO;
import com.laoliu.cas.appointment.interfaces.dto.response.BookResultResponse;
import com.laoliu.cas.common.api.GetUserIdViaTokenApi;
import com.laoliu.cas.common.result.CommonResult;
import com.laoliu.cas.system.api.dto.UserInfoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 用户端预约接口。
 *
 * @author forever-king
 */
@Tag(name = "预约服务（用户）")
@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookAppController {

    private final BookService bookService;
    private final GetUserIdViaTokenApi getUserIdViaTokenApi;

    @Operation(summary = "预定服务", description = "用户预约多个服务，传入服务ID列表")
    @PostMapping
    public CommonResult<BookResultResponse> bookService(
            @Parameter(description = "服务ID列表", required = true) @RequestParam List<Long> serviceIds) {
        Long userId = getUserIdViaTokenApi.getUserId();
        UserInfoDTO userInfo = bookService.bookService(userId, serviceIds);

        BookResultResponse response = new BookResultResponse();
        response.setUsername(userInfo.getName());
        response.setEmail(userInfo.getEmail());
        response.setGrade(userInfo.getGrade());
        response.setAllBookedServices(bookService.getAllBookings(userId));
        return CommonResult.success("预约成功", response);
    }

    @Operation(summary = "查看所有预约", description = "获取当前用户的所有预约记录")
    @GetMapping("/allService")
    public CommonResult<List<BookingDTO>> getBook() {
        Long userId = getUserIdViaTokenApi.getUserId();
        List<BookingDTO> bookings = bookService.getAllBookings(userId);
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
