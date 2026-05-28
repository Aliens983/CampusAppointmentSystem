package com.laoliu.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.laoliu.system.entity.FileEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件信息Mapper
 */
@Mapper
public interface FileMapper extends BaseMapper<FileEntity> {
}
