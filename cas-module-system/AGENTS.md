# AGENTS.md - cas-module-system

## OVERVIEW

cas-module-system 是系统管理模块，采用 DDD 四层架构，提供用户、角色、权限等系统管理功能。

## STRUCTURE

```
cas-module-system/
├── src/main/java/com/laoliu/cas/system/
│   ├── interfaces/              # 接口层
│   │   ├── controller/        # REST控制器（admin目录）
│   │   ├── dto/               # 数据传输对象
│   │   └── assembler/         # DTO转换器
│   │
│   ├── application/            # 应用层
│   │   └── service/           # 应用服务
│   │
│   ├── domain/                # 领域层
│   │   ├── entity/            # 实体
│   │   └── repository/        # 仓储接口
│   │
│   ├── infrastructure/         # 基础设施层
│   │   └── persistence/       # 持久化
│   │
│   └── api/                   # 跨模块API
```

## KEY COMPONENTS

| 类名 | 层级 | 职责 |
|------|------|------|
| UserController | interfaces | 用户管理REST接口 |
| UserService | application | 用户服务接口 |
| UserServiceImpl | application | 用户服务实现 |
| UserMapper | infrastructure | MyBatis Mapper |

## DEPENDENCIES

- 依赖: cas-module-infra, cas-framework
- 不依赖: cas-module-appointment (业务模块)

## CONVENTIONS

1. 控制器放在 interfaces.controller.admin 包下
2. 使用 @RequireRole 注解进行权限控制
3. DTO 使用 request/response 子包区分

## ANTI-PATTERNS

- 禁止在业务模块中直接操作数据库
- 禁止跨模块直接调用 Mapper
