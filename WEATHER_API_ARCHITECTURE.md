# 天气接口架构说明

## 项目结构

```
com.laoliu.system
├── config
│   └── RestTemplateConfig.java          # RestTemplate配置类
├── controller
│   └── WeatherController.java           # 天气接口控制器
├── api
│   └── api
│       └── thirdparty
│           └── weather
│               ├── WeatherApiService.java           # 天气API服务接口
│               ├── WeatherApiServiceImpl.java       # 天气API服务实现
│               └── Weathear.java                    # 占位接口
└── vo
    └── response
        └── WeatherResponse.java                         # 天气响应VO
```

## 各层职责

### 1. Controller层 (com.laoliu.system.controller)
- **WeatherController**: 处理HTTP请求，参数验证，返回CommonResult包装的响应

### 2. API层 (com.laoliu.system.api.api.thirdparty.weather)
- **WeatherApiService**: 定义第三方API调用接口
- **WeatherApiServiceImpl**: 实现第三方API调用逻辑，使用RestTemplate发送请求

### 3. VO层 (com.laoliu.system.vo.response)
- **WeatherResponse**: 响应数据模型，映射API返回的JSON结构

### 4. Config层 (com.laoliu.system.config)
- **RestTemplateConfig**: 配置RestTemplate Bean

## 配置项

在application.properties中配置：
```properties
# 天气API配置
weather.api.url=https://cn.apihz.cn/api/tianqi/tqyb.php
weather.api.id=10015369
weather.api.key=8ea9cd7dfd4a44bf805891621b69b027
```

## 使用示例

```java
// Controller层调用
@GetMapping
public CommonResult<WeatherResponse> getWeatherInfo(
        @RequestParam String sheng,
        @RequestParam String place) {
    // 参数验证
    if (!StringUtils.hasText(sheng) || !StringUtils.hasText(place)) {
        throw new BusinessException("省份和城市参数不能为空");
    }
    // 调用API服务
    return CommonResult.success(weatherApiService.getWeather(sheng, place));
}
```

## 异常处理

- 参数验证异常：BusinessException
- API调用异常：捕获RestClientException并转换为BusinessException
- API返回异常：检查响应码，非200抛出BusinessException

## 日志记录

- 调用API前记录：省份和城市信息
- 成功时记录：城市和天气信息
- 异常时记录：详细错误信息