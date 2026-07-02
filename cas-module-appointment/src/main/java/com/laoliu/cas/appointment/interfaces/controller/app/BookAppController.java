package com.laoliu.cas.appointment.interfaces.controller.app;

import com.laoliu.cas.appointment.application.service.BookService;
import com.laoliu.cas.appointment.interfaces.dto.request.SpecializedBookingRequest;
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

import java.util.Collections;
import java.util.List;

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
        return CommonResult.success("预约成功", buildBookResult(userInfo, userId));
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

    @Operation(summary = "获取预约详情", description = "根据预约ID获取单条预约的详细信息")
    @GetMapping("/allService/{id}")
    public CommonResult<BookingDTO> getBookingDetail(@PathVariable Long id) {
        BookingDTO booking = bookService.getBookingById(id);
        if (booking == null) {
            return CommonResult.notFound("预约记录不存在");
        }
        return CommonResult.success(booking);
    }

    @Operation(summary = "预约会议室", description = "预约会议室服务")
    @PostMapping("/room")
    public CommonResult<BookResultResponse> bookRoom(@RequestBody SpecializedBookingRequest request) {
        Long serviceId = request.extractServiceId();
        if (serviceId == null) {
            return CommonResult.badRequest("会议室ID不能为空");
        }
        return doSpecializedBooking(serviceId, "会议室预约成功");
    }

    @Operation(summary = "预约设备", description = "预约设备借用服务")
    @PostMapping("/equipment")
    public CommonResult<BookResultResponse> bookEquipment(@RequestBody SpecializedBookingRequest request) {
        Long serviceId = request.extractServiceId();
        if (serviceId == null) {
            return CommonResult.badRequest("设备ID不能为空");
        }
        return doSpecializedBooking(serviceId, "设备预约成功");
    }

    @Operation(summary = "预约咨询", description = "预约咨询服务")
    @PostMapping("/consultation")
    public CommonResult<BookResultResponse> bookConsultation(@RequestBody SpecializedBookingRequest request) {
        Long serviceId = request.extractServiceId();
        if (serviceId == null) {
            return CommonResult.badRequest("咨询师ID不能为空");
        }
        return doSpecializedBooking(serviceId, "咨询预约成功");
    }

    /**
     * 执行专项预约并构建响应
     */
    private CommonResult<BookResultResponse> doSpecializedBooking(Long serviceId, String successMsg) {
        Long userId = getUserIdViaTokenApi.getUserId();
        UserInfoDTO userInfo = bookService.bookService(userId, Collections.singletonList(serviceId));
        return CommonResult.success(successMsg, buildBookResult(userInfo, userId));
    }

    /**
     * 构建预约结果响应
     */
    private BookResultResponse buildBookResult(UserInfoDTO userInfo, Long userId) {
        return BookResultResponse.builder()
                .username(userInfo.getName())
                .email(userInfo.getEmail())
                .grade(userInfo.getGrade())
                .allBookedServices(bookService.getAllBookings(userId))
                .build();
    }
}
