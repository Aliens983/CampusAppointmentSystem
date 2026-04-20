package com.laoliu.system.controller;

import com.laoliu.system.api.api.thirdparty.weather.WeatherApiService;
import com.laoliu.system.common.result.CommonResult;
import com.laoliu.system.exception.BusinessException;
import com.laoliu.system.vo.response.WeatherResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 天气接口控制器
 * 
 * @author System
 * @since 2026-04-13
 */
@RestController
@RequestMapping("/api/weather")
@Api(tags = "天气接口")

// TODO : 可以去学习调用市面上常见的第三方接口
public class WeatherController {

    private final WeatherApiService weatherApiService;

    @Autowired
    public WeatherController(WeatherApiService weatherApiService) {
        this.weatherApiService = weatherApiService;
    }

    @GetMapping
    @ApiOperation("获取天气信息")
    public CommonResult<WeatherResponse> getWeatherInfo(
            @RequestParam String sheng,
            @RequestParam String place) {
        if (!StringUtils.hasText(sheng) || !StringUtils.hasText(place)) {
            throw new BusinessException("省份和城市参数不能为空");
        }
        return CommonResult.success(weatherApiService.getWeather(sheng, place));
    }
}
