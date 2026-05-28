package com.laoliu.system.service;

import com.laoliu.system.vo.request.ChatReqVO;
import com.laoliu.system.vo.response.ChatRespVO;

/**
 * @author 25516
 */
public interface CallModelService {

    ChatRespVO callQwenModel(Long userId,ChatReqVO request);
}
