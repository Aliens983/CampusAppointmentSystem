package com.laoliu.system.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laoliu.system.config.QwenConfig;
import com.laoliu.system.entity.AiChatHistory;
import com.laoliu.system.mapper.AiChatHistoryMapper;
import com.laoliu.system.service.CallModelService;
import com.laoliu.system.vo.request.ChatReqVO;
import com.laoliu.system.vo.response.ChatRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 25516
 */
@Slf4j
@Service
public class CallModelServiceImpl implements CallModelService {

    private final QwenConfig qwenConfig;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final AiChatHistoryMapper aiChatHistoryMapper;

    public CallModelServiceImpl(QwenConfig qwenConfig, AiChatHistoryMapper aiChatHistoryMapper) {
        this.qwenConfig = qwenConfig;
        this.aiChatHistoryMapper = aiChatHistoryMapper;
    }

    @Override
    public ChatRespVO callQwenModel(Long userId,ChatReqVO request) {
        long startTime = System.currentTimeMillis();
        try {
            log.info("开始调用Qwen API，请求消息: {}", request.getMessage());

            // 构建WebClient
            WebClient webClient = WebClient.builder()
                    .baseUrl(qwenConfig.getApiUrl())
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultHeader("Authorization", "Bearer " + qwenConfig.getApiKey())
                    .build();

            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();

            // 设置模型参数
            requestBody.put("model", request.getModel());

            // 设置输入参数
            Map<String, Object> input = new HashMap<>();
            Map<String, Object> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", "你是一个幽默的AI助手知识渊博,要友好解答问题.");

            Map<String, Object> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", request.getMessage());

            input.put("messages", new Object[]{systemMessage, userMessage});
            requestBody.put("input", input);

            // 设置参数
            Map<String, Object> parameters = new HashMap<>();
            // 返回格式为message
            parameters.put("result_format", "message");
            requestBody.put("parameters", parameters);

            // 发送请求并获取响应
            
            String jsonResponse = webClient.post()
                    .uri("")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    // 在实际生产环境中，建议使用异步方式
                    .block();

            log.info("Qwen API响应: {}", jsonResponse);

            // 解析响应
            JsonNode responseNode = objectMapper.readTree(jsonResponse);
            JsonNode choicesNode = responseNode.path("output").path("choices");

            if (choicesNode.isArray() && !choicesNode.isEmpty()) {
                JsonNode messageNode = choicesNode.get(0).path("message");
                String content = messageNode.path("content").asText();

                log.info("成功解析Qwen响应，内容长度: {}", content.length());

                long endTime = System.currentTimeMillis();
                int responseTimeMs = (int) (endTime - startTime);
                String model = request.getModel();
                aiChatHistoryMapper.insert(new AiChatHistory( null, userId,model , request.getMessage(), content, responseTimeMs, LocalDateTime.now(), LocalDateTime.now()));
                return new ChatRespVO(content, request.getModel(), responseTimeMs);
            } else {
                log.error("无法从响应中提取内容，响应格式可能不正确");
                return new ChatRespVO(false, "无法从API响应中提取内容");
            }
        } catch (Exception e) {
            log.error("调用Qwen API时发生错误: ", e);
            return new ChatRespVO(false, "调用Qwen API失败: " + e.getMessage());
        }
    }
}
