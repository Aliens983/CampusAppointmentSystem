package com.laoliu.cas.thirdparty.api.impl;

import com.laoliu.cas.common.exception.BusinessException;
import com.laoliu.cas.common.exception.code.CommonErrorCode;
import com.laoliu.cas.thirdparty.api.WeatherApi;
import com.laoliu.cas.thirdparty.interfaces.dto.response.WeatherResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author forever-king
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherApiImpl implements WeatherApi {

    private final RestTemplate restTemplate;

    @Value("${weather.api.url}")
    private String weatherApiUrl;

    @Value("${weather.api.id}")
    private String apiId;

    @Value("${weather.api.key}")
    private String apiKey;

    @Override
    @Cacheable(value = "weather", key = "#sheng + ':' + #place")
    public WeatherResponse getWeather(String sheng, String place) {
        try {
            log.info("调用天气API - 省份: {}, 城市: {}", sheng, place);

            Map<String, String> params = new HashMap<>();
            params.put("id", apiId);
            params.put("key", apiKey);
            params.put("sheng", sheng);
            params.put("place", place);

            StringBuilder urlBuilder = new StringBuilder(weatherApiUrl);
            urlBuilder.append("?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            String url = urlBuilder.toString().replaceAll("&$", "");

            log.info("请求URL: {}", url);

            WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);

            if (response == null || response.getCode() != 200) {
                throw new BusinessException(500, "获取天气信息失败");
            }

            return response;
        } catch (Exception e) {
            log.error("获取天气信息失败", e);
            throw new BusinessException(CommonErrorCode.WEATHER_QUERY_FAILED);
        }
    }
}
