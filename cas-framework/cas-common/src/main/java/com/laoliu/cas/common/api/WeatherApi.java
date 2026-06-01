package com.laoliu.cas.common.api;

public interface WeatherApi {

    WeatherResponse getWeather(String sheng, String place);
}