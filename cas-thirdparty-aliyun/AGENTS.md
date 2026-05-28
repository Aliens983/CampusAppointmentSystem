# AGENTS.md - cas-thirdparty-aliyun

## OVERVIEW

cas-thirdparty-aliyun 是第三方集成模块，隔离阿里云等第三方系统接口调用。

## STRUCTURE

```
cas-thirdparty-aliyun/
├── src/main/java/com/laoliu/cas/thirdparty/aliyun/
│   ├── service/      # 服务接口和实现
│   └── config/       # 配置类
```

## KEY COMPONENTS

| 类名 | 职责 |
|------|------|
| SmsService | 短信服务接口 |
| SmsServiceImpl | 短信服务实现（阿里云SMS） |

## DEPENDENCIES

- 依赖: cas-common (保持轻量)
- 不依赖: 任何业务模块

## CONVENTIONS

1. 第三方模块保持轻量，只依赖 common
2. 通过接口暴露服务，便于后续替换实现
3. 配置通过 @Value 注解读取
