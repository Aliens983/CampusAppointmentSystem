package com.laoliu.cas.appointment.application.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.laoliu.cas.appointment.application.service.BookService;
import com.laoliu.cas.appointment.domain.repository.BookingRepository;
import com.laoliu.cas.appointment.domain.repository.ServiceRepository;
import com.laoliu.cas.appointment.interfaces.dto.response.BookingDTO;
import com.laoliu.cas.appointment.interfaces.dto.response.ServiceStatusResponse;
import com.laoliu.cas.common.exception.BusinessException;
import com.laoliu.cas.common.exception.code.BookErrorCode;
import com.laoliu.cas.common.exception.code.ServiceErrorCode;
import com.laoliu.cas.system.api.UserInfoApi;
import com.laoliu.cas.system.api.dto.UserInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 预约业务应用服务实现
 *
 * @author forever-king
 */
@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final BookingRepository bookingRepository;
    private final ServiceRepository serviceRepository;
    private final UserInfoApi userInfoApi;

    public BookServiceImpl(BookingRepository bookingRepository, ServiceRepository serviceRepository, UserInfoApi userInfoApi) {
        this.bookingRepository = bookingRepository;
        this.serviceRepository = serviceRepository;
        this.userInfoApi = userInfoApi;
    }

    @Override
    @Transactional
    public UserInfoDTO bookService(Long userId, List<Long> serviceIds) {
        if (serviceIds == null || serviceIds.isEmpty()) {
            throw new BusinessException(ServiceErrorCode.SERVICE_ID_EMPTY);
        }

        for (Long sid : serviceIds) {
            com.laoliu.cas.appointment.domain.entity.Service service = serviceRepository.findById(sid)
                    .orElseThrow(() -> new BusinessException(ServiceErrorCode.SERVICE_NOT_EXIST, sid));
            if (!service.isAvailable()) {
                throw new BusinessException(ServiceErrorCode.SERVICE_DISABLED, sid);
            }
        }

        try {
            List<Integer> serviceIdInts = serviceIds.stream()
                    .map(Long::intValue)
                    .collect(Collectors.toList());
            bookingRepository.insertServices(userId, serviceIdInts);
            return userInfoApi.getUserById(userId);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("预约失败: userId={}, serviceIds={}", userId, serviceIds, e);
            throw new BusinessException(BookErrorCode.BOOKING_FAILED);
        }
    }

    @Override
    public List<BookingDTO> getAllBookings(Long userId) {
        return bookingRepository.getServiceStatusByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public IPage<BookingDTO> getAllBookings(Long userId, int page, int pageSize) {
        IPage<ServiceStatusResponse> statusPage = bookingRepository.getServiceStatusByUserId(
                userId, page, pageSize, null, null);
        return statusPage.convert(this::convertToDTO);
    }

    private BookingDTO convertToDTO(ServiceStatusResponse status) {
        BookingDTO dto = new BookingDTO();
        dto.setOrderId(status.getOrderId());
        dto.setUserId(status.getUserId());
        dto.setServiceName(status.getServiceName());
        dto.setStatus(status.getManageStatus());
        dto.setCreateTime(status.getCreateTime());
        dto.setReason(status.getStatusDescription());
        dto.setStatusDescription(getStatusDescription(status.getManageStatus()));
        return dto;
    }

    private String getStatusDescription(Integer status) {
        if (status == null) {
            return "未知状态";
        }
        return switch (status) {
            case 0 -> "待审核";
            case 1 -> "通过";
            case 2 -> "拒绝";
            case 3 -> "取消";
            default -> "未知状态";
        };
    }

    @Override
    @Transactional
    public boolean cancelBookings(Long userId, List<Long> bookingIds) {
        if (bookingIds == null || bookingIds.isEmpty()) {
            return false;
        }
        return bookingRepository.cancelBookings(userId, bookingIds) > 0;
    }

    @Override
    public BookingDTO getBookingById(Long orderId) {
        ServiceStatusResponse status = bookingRepository.getServiceStatusByOrderId(orderId);
        if (status == null) {
            return null;
        }
        return convertToDTO(status);
    }
}
