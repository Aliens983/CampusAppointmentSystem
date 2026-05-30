package com.laoliu.cas.system.interfaces.controller;

import com.laoliu.cas.common.result.CommonResult;
import com.laoliu.cas.common.service.CallModelService;
import com.laoliu.cas.common.vo.request.ChatReqVO;
import com.laoliu.cas.common.vo.response.ChatRespVO;
import com.laoliu.cas.common.api.GetUserIdViaTokenApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "大模型调用")
@RestController
@RequestMapping("/callTheLargeModel")
@RequiredArgsConstructor
public class CallTheModelController {

    private final CallModelService qwenService;
    private final GetUserIdViaTokenApi getUserIdViaTokenApi;

    @RequestMapping("/callTheModel/qwen")
    @Operation(summary = "调用Qwen大模型")
    public CommonResult<ChatRespVO> chatWithQwen(@RequestBody ChatReqVO request, HttpServletRequest userRequest) {
        Long userId = getUserIdViaTokenApi.getUserId(userRequest);

        log.info("收到聊天请求，消息: {},用户ID:{}", request.getMessage(), userId);

        if (request.getModel() == null || request.getModel().trim().isEmpty()) {
            request.setModel("qwen-plus");
        }

        ChatRespVO response = qwenService.callQwenModel(userId, request);
        log.info("返回聊天响应，成功: {}, 模型: {}", response.isSuccess(), response.getModel());
        return CommonResult.success(response);
    }
}
