package com.laoliu.cas.appointment.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.laoliu.cas.common.domain.entity.Services;
import com.laoliu.cas.appointment.infrastructure.persistence.dataobject.ServicesDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author forever-king
 */
@Mapper
public interface ServicesMapper extends BaseMapper<Services> {

    @Select("SELECT * FROM services WHERE service_state = 1")
    List<ServicesDO> selectEnabledServices();

    @Select("SELECT * FROM services WHERE service_id = #{serviceId}")
    ServicesDO selectByServiceId(@Param("serviceId") Integer serviceId);

    int insertSelective(Services services);
}
