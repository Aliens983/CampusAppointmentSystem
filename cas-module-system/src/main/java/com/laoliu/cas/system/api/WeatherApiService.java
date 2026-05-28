package com.laoliu.cas.system.api;

import com.laoliu.cas.system.interfaces.dto.response.WeatherResponse;

public interface WeatherApiService {

    WeatherResponse getWeather(String sheng, String place);
}
