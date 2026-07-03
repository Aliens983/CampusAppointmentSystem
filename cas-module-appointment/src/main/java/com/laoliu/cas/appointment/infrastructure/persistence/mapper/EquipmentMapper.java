package com.laoliu.cas.appointment.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.laoliu.cas.appointment.infrastructure.persistence.dataobject.EquipmentDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 设备 Mapper
 *
 * @author forever-king
 */
@Mapper
public interface EquipmentMapper extends BaseMapper<EquipmentDO> {

    @Select("SELECT * FROM equipment WHERE service_id = #{serviceId}")
    List<EquipmentDO> findByServiceId(@Param("serviceId") Long serviceId);
}
