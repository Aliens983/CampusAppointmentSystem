package com.laoliu.cas.thirdparty.interfaces;

import com.laoliu.cas.common.api.WeatherApi;
import com.laoliu.cas.common.api.WeatherResponse;
import com.laoliu.cas.common.result.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author forever-king
 */
@Tag(name = "天气接口")
@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherApi weatherApi;

    @Operation(summary = "获取天气信息", description = "根据省份和城市名称获取天气信息")
    @GetMapping
    public CommonResult<WeatherResponse> getWeatherInfo(
            @Parameter(description = "省份名称", required = true) @RequestParam String sheng,
            @Parameter(description = "城市名称", required = true) @RequestParam String place) {
        if (!StringUtils.hasText(sheng) || !StringUtils.hasText(place)) {
            return CommonResult.badRequest("省份和城市参数不能为空");
        }
        return CommonResult.success(weatherApi.getWeather(sheng, place));
    }
}