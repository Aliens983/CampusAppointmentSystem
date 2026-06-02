package com.laoliu.cas.thirdparty.service;

import com.laoliu.cas.thirdparty.api.vo.request.ChatReqVO;
import com.laoliu.cas.thirdparty.api.vo.response.ChatRespVO;

/**
 * @author forever-king
 */
public interface CallModelService {
    ChatRespVO callQwenModel(Long userId, ChatReqVO request);
}
