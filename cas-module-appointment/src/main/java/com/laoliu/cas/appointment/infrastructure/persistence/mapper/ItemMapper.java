package com.laoliu.cas.appointment.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.laoliu.cas.appointment.infrastructure.persistence.dataobject.ItemDO;
import com.laoliu.cas.appointment.infrastructure.persistence.dataobject.ServicesDO;
import com.laoliu.cas.appointment.interfaces.dto.response.ServiceStatusResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ItemMapper extends BaseMapper<ItemDO> {

    void setBookingStatus(@Param("userId") Long userId, @Param("bookingId") Long bookingId);

    void insertServices(@Param("userId") Long userId, @Param("serviceId") List<Integer> serviceId);

    int setBookingStatusByParts(@Param("userId") Long userId, @Param("bookingIds") List<Long> bookingIds);

    List<ServiceStatusResponse> getServiceStatus();

    List<ServiceStatusResponse> getServiceStatusByUserId(@Param("userId") Long userId);

    int auditService(@Param("orderId") Long orderId, @Param("status") Integer status, @Param("reason") String reason);

    ServiceStatusResponse getServiceStatusByOrderId(Long orderId);

    String getUserEmailByOrderId(@Param("orderId") Long orderId);

    List<ServicesDO> selectUserServices(Long userId);
}
