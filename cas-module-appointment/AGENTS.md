# AGENTS.md - cas-module-appointment

## OVERVIEW

cas-module-appointment 是预约业务模块，采用 DDD 四层架构，提供服务预约等业务功能。

## STRUCTURE

```
cas-module-appointment/
├── src/main/java/com/laoliu/cas/appointment/
│   ├── interfaces/              # 接口层
│   │   └── controller/        # REST控制器
│   ├── application/            # 应用层
│   │   └── service/           # 应用服务
│   ├── domain/                # 领域层
│   │   ├── entity/          # 实体
│   │   └── repository/      # 仓储接口
│   └── infrastructure/      # 基础设施层
│       └── persistence/    # 持久化
```

## KEY COMPONENTS

| 类名 | 层级 | 职责 |
|------|------|------|
| ServiceController | interfaces | 服务REST接口 |
| ServicesService | application | 服务应用服务 |
| ServicesMapper | infrastructure | MyBatis Mapper |

## DEPENDENCIES

- 依赖: cas-module-system, cas-module-infra, cas-framework
- 被依赖: cas-server (启动入口)

## CONVENTIONS

1. 业务模块采用 DDD 四层架构
2. 依赖 system 模块获取用户信息
3. 使用 infra 模块的文件服务

## ANTI-PATTERNS

- 禁止直接依赖其他业务模块
- 禁止在应用层直接操作数据库
