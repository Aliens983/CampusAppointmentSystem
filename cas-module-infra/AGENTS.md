# AGENTS.md - cas-module-infra

## OVERVIEW

cas-module-infra 是基础设施模块，采用 DDD 四层架构，为业务模块提供通用基础设施服务，包括文件服务、代码生成等。

## STRUCTURE

```
cas-module-infra/
├── src/main/java/com/laoliu/cas/infra/
│   ├── interfaces/              # 接口层
│   │   ├── controller/         # REST控制器
│   │   └── dto/                # 数据传输对象
│   │
│   ├── application/            # 应用层
│   │   └── service/            # 应用服务
│   │
│   ├── domain/                 # 领域层
│   │   ├── entity/             # 实体
│   │   └── repository/         # 仓储接口
│   │
│   ├── infrastructure/         # 基础设施层
│   │   ├── persistence/        # 持久化
│   │   └── external/           # 外部服务
│   │
│   └── api/                    # 跨模块API
│
└── src/main/resources/
```

## KEY COMPONENTS

| 类名 | 层级 | 职责 |
|------|------|------|
| FileController | interfaces | 文件上传REST接口 |
| FileService | application | 文件服务接口 |
| FileServiceImpl | application | 文件服务实现 |

## DEPENDENCIES

- 依赖: cas-framework (技术底座)
- 不依赖: cas-module-system, cas-module-appointment (业务模块)

## CONVENTIONS

1. 基础设施模块不依赖任何业务模块
2. 采用依赖倒置：领域层定义仓储接口，基础设施层实现
3. 应用层编排领域对象，不包含业务规则

## ANTI-PATTERNS

- 禁止在 infra 模块中引入业务相关的类
- 禁止在基础设施层直接调用业务模块的接口
- 禁止在领域层依赖 Spring 框架注解
