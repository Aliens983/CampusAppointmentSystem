package com.laoliu.cas.common.service;

import com.laoliu.cas.common.vo.request.ChatReqVO;
import com.laoliu.cas.common.vo.response.ChatRespVO;

public interface CallModelService {
    ChatRespVO callQwenModel(Long userId, ChatReqVO request);
}
