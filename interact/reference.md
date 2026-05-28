一、整体项目结构
项目采用 Maven 多模块分层架构，整体结构如下：

PlainText



项目根目录├── xxx-dependencies          # BOM 依赖版本管理├── xxx-framework             # 技术框架层（自定义 Spring Boot Starter）│   ├── xxx-common            # 公共模块（异常、枚举、工具类、跨模块 API 接口）│   ├── xxx-spring-boot-starter-web        # Web 层封装│   ├── xxx-spring-boot-starter-security   # 安全认证│   ├── xxx-spring-boot-starter-mybatis    # 数据访问│   ├── xxx-spring-boot-starter-redis      # 缓存│   ├── xxx-spring-boot-starter-mq         # 消息队列│   ├── xxx-spring-boot-starter-job        # 定时任务│   ├── xxx-spring-boot-starter-excel      # Excel 导入导出│   ├── xxx-spring-boot-starter-websocket  # WebSocket│   ├── xxx-spring-boot-starter-monitor    # 监控│   ├── xxx-spring-boot-starter-protection # 服务保障│   ├── xxx-spring-boot-starter-test       # 测试基类│   ├── xxx-spring-boot-starter-biz-tenant # 多租户│   ├── xxx-spring-boot-starter-biz-data-permission # 数据权限│   └── xxx-spring-boot-starter-biz-ip     # IP 属地├── xxx-module-infra          # 基础设施模块├── xxx-module-system         # 系统管理模块├── xxx-module-pay            # 支付模块├── xxx-module-[业务名]       # 核心业务模块├── xxx-thirdparty-[名称]     # 第三方接口集成模块└── xxx-server                # 启动入口模块
二、模块职责说明
模块类型	模块名称	职责
版本管理	dependencies	统一管理所有第三方依赖版本，避免版本冲突
技术框架	framework	提供技术基础设施，每个子模块是一个 Spring Boot Starter
基础设施	module-infra	提供运维工具和研发工具（代码生成器、文件服务、监控等）
系统管理	module-system	提供通用业务能力（用户、权限、字典、认证、通知等）
业务模块	module-xxx	按业务领域划分，采用 DDD 分层结构
第三方集成	thirdparty-xxx	隔离第三方系统接口，避免污染核心业务代码
启动入口	server	空壳容器，聚合所有模块，包含启动类和配置文件
三、依赖层级关系
PlainText



层级 0: dependencies (BOM 版本管理)层级 1: framework (技术底座，不依赖任何业务模块)层级 2: module-infra, thirdparty-xxx (基础设施，只依赖 framework)层级 3: module-system (系统管理，依赖 infra 和 thirdparty)层级 4: module-pay, module-[业务] (业务模块，依赖 system)层级 5: server (启动入口，聚合所有业务模块)
依赖原则：上层依赖下层，下层不知道上层存在，单向依赖，禁止循环依赖。

四、业务模块 DDD 分层结构
每个业务模块（如 module-system、module-hotel、module-pay）内部采用 DDD 四层架构：

PlainText



xxx-module-[业务名]/├── src/main/java/com/xxx/module/[业务名]/│   ├── interfaces/              # 接口层（用户界面层）│   │   ├── controller/          # REST 控制器│   │   │   ├── admin/           # 管理端接口│   │   │   └── app/             # 应用端/用户端接口│   │   ├── dto/                 # 数据传输对象│   │   │   ├── request/         # 请求 DTO│   │   │   └── response/        # 响应 DTO│   │   └── assembler/           # DTO 与领域对象转换器│   ││   ├── application/             # 应用层（用例编排层）│   │   ├── service/             # 应用服务（编排领域对象，协调业务流程）│   │   ├── command/             # 命令对象（写操作的输入）│   │   ├── query/               # 查询对象（读操作的输入）│   │   └── event/               # 应用层事件（如发送通知等）│   ││   ├── domain/                  # 领域层（核心业务逻辑）│   │   ├── entity/              # 实体（有唯一标识，状态可变）│   │   ├── valueobject/         # 值对象（无标识，不可变）│   │   ├── aggregate/           # 聚合（实体集合，数据修改的单元）│   │   ├── service/             # 领域服务（不属于单一实体的业务逻辑）│   │   ├── repository/          # 仓储接口（领域层定义，基础设施层实现）│   │   ├── event/               # 领域事件（实体状态变化触发的事件）│   │   └── specification/       # 规格模式（业务规则封装）│   ││   ├── infrastructure/          # 基础设施层（技术实现）│   │   ├── persistence/         # 持久化实现│   │   │   ├── repository/      # 仓储实现│   │   │   ├── mapper/          # MyBatis Mapper│   │   │   ├── dataobject/      # 数据对象（DO，数据库映射）│   │   │   └── converter/       # DO 与实体转换器│   │   ├── external/            # 外部服务调用│   │   │   ├── rpc/             # RPC 调用│   │   │   └── http/            # HTTP 调用│   │   ├── mq/                  # 消息队列│   │   │   ├── producer/        # 消息生产者│   │   │   └── consumer/        # 消息消费者│   │   └── config/              # 基础设施配置│   ││   ├── api/                     # 跨模块 API（供其他模块调用）│   │   ├── dto/                 # API 数据传输对象│   │   └── XxxApi.java          # API 接口定义│   │   └── XxxApiImpl.java      # API 接口实现│   ││   └── enums/                   # 模块内枚举│├── src/test/java/               # 测试代码│   ├── unit/                    # 单元测试（测试领域层）│   └── integration/             # 集成测试（测试应用层 + 基础设施层）│├── AGENTS.md                    # 模块说明文档（给 AI 工具看）├── README.md                    # 模块说明文档（给人看）└── pom.xml
五、DDD 各层职责
层级	包名	职责	依赖方向
接口层	interfaces	接收请求、参数校验、调用应用层、返回响应	→ application
应用层	application	编排领域对象、协调业务流程、事务控制	→ domain
领域层	domain	核心业务逻辑、实体、值对象、领域服务、仓储接口	无外部依赖
基础设施层	infrastructure	仓储实现、外部服务调用、消息队列、技术配置	→ domain（实现仓储接口）
关键原则：

领域层不依赖任何外层，纯业务逻辑，可独立测试
基础设施层实现领域层定义的仓储接口（依赖倒置）
应用层编排领域对象，不包含业务规则
接口层只做参数转换和调用应用层，不包含业务逻辑
六、跨模块调用方式
模块间通过 api/ 包的接口解耦：

Java



// 模块 A 调用模块 B 的用户服务// 1. 模块 B 定义 API 接口public interface AdminUserApi {    AdminUserRespDTO getUser(Long userId);}// 2. 模块 B 实现 API 接口@Componentpublic class AdminUserApiImpl implements AdminUserApi {    @Autowired    private UserApplicationService userService;        public AdminUserRespDTO getUser(Long userId) {        return userService.getUser(userId);    }}// 3. 模块 A 通过接口调用@Lazy  // 防止循环依赖@Autowiredprivate AdminUserApi adminUserApi;public void doSomething() {    AdminUserRespDTO user = adminUserApi.getUser(userId);}
## 七、模块级文档规范
每个模块必须包含 AGENTS.md 和 README.md 文档：

### AGENTS.md 模板（给 AI 工具看）
```
## OVERVIEW
- [模块名称] 模块，负责 [核心职责描述]
- 支持能力：[能力1]、[能力2]、[能力3]

## STRUCTURE
- 包路径: com.xxx.module.[业务名]
- DDD 分层:
  - interfaces/    # 接口层
  - application/   # 应用层
  - domain/        # 领域层
  - infrastructure/ # 基础设施层
  - api/           # 跨模块 API

## KEY COMPONENTS
- [核心类1]: [职责说明]
- [核心类2]: [职责说明]
- [核心类3]: [职责说明]

## DEPENDENCIES
- 依赖模块: [模块1]、[模块2]
- 依赖框架: [starter1]、[starter2]

## CONVENTIONS
- [规范1]
- [规范2]
- [规范3]

## ANTI-PATTERNS
- [反模式1]: [原因]
- [反模式2]: [原因]
```
### README.md 模板（给人看）
```
# xxx-module-[业务名]

## 模块职责
[描述模块的核心职责和业务范围]

## 核心功能
- [功能1]：[说明]
- [功能2]：[说明]
- [功能3]：[说明]

## 目录结构
[目录树形结构]

## 核心类说明
| 类名 | 职责 | 所在层 |
|------|------|--------|
| XxxEntity | [说明] | domain |
| XxxRepository | [说明] | domain |
| XxxApplicationService | [说明] | application |
| XxxController | [说明] | interfaces |

## 依赖关系
- 依赖模块：xxx-module-system、xxx-module-infra
- 依赖框架：starter-web、starter-mybatis、starter-redis

## 使用示例
[代码示例]

## 注意事项
- [注意点1]
- [注意点2]
```
## 八、设计原则
1. 单一职责 ：每个模块只负责一个业务领域
2. 依赖倒置 ：模块间通过接口调用，领域层定义仓储接口，基础设施层实现
3. 开闭原则 ：新增功能通过新增模块/类实现，不修改已有代码
4. 接口隔离 ：跨模块调用通过 api/ 接口，不直接调用 Service
5. 单向依赖 ：依赖关系从上到下，禁止循环依赖
6. 领域纯度 ：领域层不依赖任何外部框架，纯业务逻辑