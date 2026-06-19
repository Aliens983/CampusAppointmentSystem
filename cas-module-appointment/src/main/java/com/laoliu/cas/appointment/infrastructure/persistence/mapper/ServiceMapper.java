package com.laoliu.cas.appointment.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.laoliu.cas.appointment.infrastructure.persistence.dataobject.ServicesDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 服务表 Mapper - 操作 services 表
 *
 * @author forever-king
 */
@Mapper
public interface ServiceMapper extends BaseMapper<ServicesDO> {

    ServicesDO selectByPrimaryKey(Long id);

    List<ServicesDO> selectAll();

    int insertSelective(ServicesDO record);

    int updateByPrimaryKeySelective(ServicesDO record);

    int updateByPrimaryKey(ServicesDO record);

    List<ServicesDO> selectUserServices(@Param("userId") Long userId);

    @Select("SELECT * FROM services WHERE service_state = 1")
    List<ServicesDO> selectEnabledServices();

    @Select("SELECT * FROM services WHERE service_id = #{serviceId}")
    ServicesDO selectByServiceId(@Param("serviceId") Long serviceId);
}
