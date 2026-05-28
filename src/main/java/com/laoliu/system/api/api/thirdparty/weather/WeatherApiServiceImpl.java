package com.laoliu.system.api.api.thirdparty.weather;

import com.laoliu.system.exception.BusinessException;
import com.laoliu.system.vo.response.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 天气API服务实现
 * 
 * @author System
 * @since 2026-04-13
 */
@Service
@Slf4j
public class WeatherApiServiceImpl implements WeatherApiService {

    private final RestTemplate restTemplate;

    @Value("${weather.api.url}")
    private String weatherApiUrl;

    @Value("${weather.api.id}")
    private String apiId;

    @Value("${weather.api.key}")
    private String apiKey;

    public WeatherApiServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public WeatherResponse getWeather(String sheng, String place) {
        try {
            log.info("调用天气API - 省份: {}, 城市: {}", sheng, place);
            log.info("天气API配置 - URL: {}, ID: {}, Key: {}", weatherApiUrl, apiId, apiKey);
            
            // 构建请求参数
            Map<String, String> params = new HashMap<>();
            params.put("id", apiId);
            params.put("key", apiKey);
            params.put("sheng", sheng);
            params.put("place", place);
            
            // 构建完整的URL，将参数作为查询参数添加
            StringBuilder urlBuilder = new StringBuilder(weatherApiUrl);
            urlBuilder.append("?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            // 移除最后一个&符号
            String url = urlBuilder.toString().replaceAll("&$", "");
            
            log.info("请求URL: {}", url);
            
            // 发送GET请求
            ResponseEntity<WeatherResponse> response = restTemplate.getForEntity(
                    url, 
                    WeatherResponse.class);
            
            log.info("响应状态码: {}", response.getStatusCode());
            log.info("响应头: {}", response.getHeaders());
            
            WeatherResponse weatherResponse = response.getBody();
            
            log.info("响应体: {}", weatherResponse);
            
            if (weatherResponse == null || weatherResponse.getCode() != 200) {
                log.error("天气API返回异常: {}", weatherResponse);
                throw new BusinessException("获取天气信息失败: " + 
                    (weatherResponse != null ? weatherResponse.getMessage() : "未知错误"));
            }
            
            log.info("成功获取天气信息 - 城市: {}, 天气: {}", weatherResponse.getShi(), weatherResponse.getWeather1());
            return weatherResponse;
        } catch (Exception e) {
            log.error("调用天气API失败", e);
            throw new BusinessException("获取天气信息失败: " + e.getMessage());
        }
    }
}