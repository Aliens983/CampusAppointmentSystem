package com.laoliu.system.api.api.thirdparty.weather;

import com.laoliu.system.vo.response.WeatherResponse;

/**
 * 天气API服务接口
 * 
 * @author System
 * @since 2026-04-13
 */
public interface WeatherApiService {

    /**
     * 获取天气信息
     * 
     * @param sheng 省份
     * @param place 城市
     * @return 天气响应
     */
    WeatherResponse getWeather(String sheng, String place);
}