package com.laoliu.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.laoliu.system.entity.Services;
import com.laoliu.system.vo.request.ServiceAddRequest;

import java.util.List;

/**
* @author 25516
* @description 针对表【service】的数据库操作Mapper
* @createDate 2026-03-16 10:31:25
* @Entity com.laoliu.system.entity.Services
*/
public interface ServiceMapper extends BaseMapper<Services> {

    int deleteByPrimaryKey(Long id);

    int insert(Services record);

    int insertSelective(ServiceAddRequest record);

    Services selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Services record);

    int updateByPrimaryKey(Services record);

    List<Services> selectAll();
}
