package com.laoliu.system.mapper;

import com.laoliu.system.entity.Item;
import com.laoliu.system.entity.Service;
import com.laoliu.system.vo.request.AuditRequest;
import com.laoliu.system.vo.response.ServiceStatusResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 25516
* @description Entity item表的数据操作Mapper
* @createDate 2026-03-16 11:42:54
* @Entity com.laoliu.system.entity.Item
*/
public interface ItemMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Item record);

    int insertSelective(Item record);

    Item selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Item record);

    int updateByPrimaryKey(Item record);

    List<Service> selectUserServices(Long userId);

    /**
     * 获取服务状态列表（管理员专用）
     * 查询所有服务预约信息，包含用户和服务详情
     * 
     * @return 服务状态列表
     */
    List<ServiceStatusResponse> getServiceStatus();

    /**
     * 根据用户ID获取服务状态
     * 
     * @param userId 用户ID
     * @return 服务状态列表
     */
    List<ServiceStatusResponse> getServiceStatusByUserId(@Param("userId") Long userId);

    /**
     * 审核服务预约
     * 
     * @param orderId 订单ID
     * @param status 审核状态（1:通过,2:拒绝）
     * @param reason 拒绝原因（可选）
     * @return 影响的行数
     */
    int auditService(@Param("orderId") Long orderId, @Param("status") Integer status, @Param("reason") String reason);

    /**
     * 根据订单ID获取服务信息（用于发送邮件通知）
     * 
     * @param orderId 订单ID
     * @return 服务状态响应对象
     */
    ServiceStatusResponse getServiceStatusByOrderId(Long orderId);

    /**
     * 根据订单ID获取用户邮箱（用于发送邮件通知）
     * 
     * @param orderId 订单ID
     * @return 用户邮箱
     */
    String getUserEmailByOrderId(Long orderId);

    void setBookingStatus(Long userId,Long bookingId);

    int insertServices(@Param("userId") Long userId, @Param("serviceId") List<Integer> serviceId);

    int setBookingStatusByParts(@Param("userId") Long userId, @Param("bookingIds") List<Long> bookingIds);

}

