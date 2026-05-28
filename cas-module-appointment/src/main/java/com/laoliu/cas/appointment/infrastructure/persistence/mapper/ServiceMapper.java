package com.laoliu.cas.appointment.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.laoliu.cas.appointment.infrastructure.persistence.dataobject.ServicesDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ServiceMapper extends BaseMapper<ServicesDO> {

    ServicesDO selectByPrimaryKey(Long id);

    List<ServicesDO> selectAll();

    int insertSelective(ServicesDO record);

    int updateByPrimaryKeySelective(ServicesDO record);

    int updateByPrimaryKey(ServicesDO record);

    List<ServicesDO> selectUserServices(@Param("userId") Long userId);
}
