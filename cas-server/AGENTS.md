# AGENTS.md - cas-server

## OVERVIEW

cas-server 是应用启动入口模块，聚合所有业务模块的依赖，配置应用启动参数。

## STRUCTURE

```
cas-server/
├── src/main/java/com/laoliu/cas/server/
│   └── CampusAppointmentApplication.java  # 启动类
├── src/main/resources/
│   └── application.yml                   # 配置文件
└── pom.xml                              # 聚合依赖
```

## KEY COMPONENTS

| 类名 | 职责 |
|------|------|
| CampusAppointmentApplication | Spring Boot启动类 |

## DEPENDENCIES

- 依赖: 所有业务模块（system, infra, appointment, thirdparty-aliyun）
- 依赖: 所有框架模块（framework starters）

## CONVENTIONS

1. 使用 @MapperScan 扫描所有 Mapper 接口
2. 配置文件统一管理在 resources 目录
3. 不编写业务代码，只做依赖聚合和配置

## ANTI-PATTERNS

- 禁止在 server 模块编写业务逻辑
- 禁止在 server 模块创建新的 domain entity
- 所有配置必须在 application.yml 中声明
