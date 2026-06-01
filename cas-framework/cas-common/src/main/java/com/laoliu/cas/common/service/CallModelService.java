package com.laoliu.cas.common.service;

import com.laoliu.cas.common.vo.request.ChatReqVO;
import com.laoliu.cas.common.vo.response.ChatRespVO;

/**
 * @author forever-king
 */
public interface CallModelService {
    ChatRespVO callQwenModel(Long userId, ChatReqVO request);
}
