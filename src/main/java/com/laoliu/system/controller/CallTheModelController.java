package com.laoliu.system.controller;

import com.laoliu.system.api.GetUserIdViaTokenApi;
import com.laoliu.system.common.result.CommonResult;
import com.laoliu.system.service.CallModelService;
import com.laoliu.system.vo.request.ChatReqVO;
import com.laoliu.system.vo.response.ChatRespVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 25516
 */
@Slf4j
@Tag(name = "大模型调用")
@RestController
@RequestMapping("/callTheLargeModel")
public class CallTheModelController {

    private final CallModelService qwenService;

    private final GetUserIdViaTokenApi getUserIdViaTokenApi;

    public CallTheModelController(CallModelService qwenService, GetUserIdViaTokenApi getUserIdViaTokenApi) {
        this.qwenService = qwenService;
        this.getUserIdViaTokenApi = getUserIdViaTokenApi;
    }

    @RequestMapping("/callTheModel/qwen")
    @Operation(summary = "调用Qwen大模型")
    public CommonResult<ChatRespVO> chatWithQwen(@RequestBody ChatReqVO request, HttpServletRequest userRequest) {

        Long userId = getUserIdViaTokenApi.getUserId(userRequest);

        log.info("收到聊天请求，消息: {},用户ID:{}", request.getMessage(), userId);

        // 如果没有指定模型，默认使用qwen-plus
        if (request.getModel() == null || request.getModel().trim().isEmpty()) {
            request.setModel("qwen-plus");
        }

        ChatRespVO response = qwenService.callQwenModel(userId,request);
        log.info("返回聊天响应，成功: {}, 模型: {}", response.isSuccess(), response.getModel());
        return CommonResult.success(response);

    }

}
