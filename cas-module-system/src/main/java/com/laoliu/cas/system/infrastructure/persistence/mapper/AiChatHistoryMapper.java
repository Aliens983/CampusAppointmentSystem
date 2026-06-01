package com.laoliu.cas.system.infrastructure.persistence.mapper;

import com.laoliu.cas.common.domain.entity.AiChatHistory;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author forever-king
 */
@Mapper
public interface AiChatHistoryMapper {
    void insert(AiChatHistory aiChatHistory);
}
