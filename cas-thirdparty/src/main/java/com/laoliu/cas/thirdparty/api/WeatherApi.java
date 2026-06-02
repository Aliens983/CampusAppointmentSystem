package com.laoliu.cas.thirdparty.api;

import com.laoliu.cas.thirdparty.api.response.WeatherResponse;

/**
 * @author forever-king
 */
public interface WeatherApi {

    WeatherResponse getWeather(String sheng, String place);
}
