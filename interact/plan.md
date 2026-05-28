# Campus Appointment System 多Maven模块改造计划

## 一、改造目标

将现有的单体Spring Boot项目改造成**企业级Maven多模块项目**，采用**DDD领域驱动设计**分层架构。

---

## 二、目标项目结构

```
CampusAppointmentSystem/
│
├── pom.xml                         # 父项目，packaging=pom
│
├── cas-dependencies/               # BOM版本管理模块
│   └── pom.xml
│
├── cas-framework/                  # 技术框架层（Spring Boot Starter）
│   ├── pom.xml
│   ├── cas-common/                 # 公共基础模块
│   ├── cas-spring-boot-starter-web/         # Web层封装
│   ├── cas-spring-boot-starter-security/     # 安全认证
│   ├── cas-spring-boot-starter-mybatis/      # 数据访问
│   ├── cas-spring-boot-starter-redis/        # 缓存
│   ├── cas-spring-boot-starter-mq/           # 消息队列
│   └── cas-spring-boot-starter-test/         # 测试基类
│
├── cas-module-infra/              # 基础设施模块（DDD结构）
├── cas-module-system/              # 系统管理模块（DDD结构）
├── cas-module-appointment/         # 预约业务模块（DDD结构）
│
├── cas-thirdparty-aliyun/          # 第三方集成（阿里云OSS、通义千问）
│
└── cas-server/                     # 启动入口模块
```

---

## 三、依赖层级关系（单向依赖，禁止循环）

```
层级 0: cas-dependencies (BOM版本管理)
    │
    ▼
层级 1: cas-framework (技术底座，不依赖任何业务模块)
    │
    ▼
层级 2: cas-module-infra, cas-thirdparty-xxx (基础设施，只依赖framework)
    │
    ▼
层级 3: cas-module-system (系统管理，依赖infra和thirdparty)
    │
    ▼
层级 4: cas-module-appointment (业务模块，依赖system和infra)
    │
    ▼
层级 5: cas-server (启动入口，聚合所有业务模块)
```

---

## 四、详细实施步骤

### 阶段1：创建根项目和cas-dependencies模块

#### 1.1 创建根项目结构
- [x] 1.1.1 创建根目录 `CampusAppointmentSystem/`
- [x] 1.1.2 创建根pom.xml（packaging=pom）
- [x] 1.1.3 配置modules标签列出所有子模块
- [x] 1.1.4 配置Java版本17、编码UTF-8
- [x] 1.1.5 保留原项目的sql、data、interact目录

#### 1.2 创建cas-dependencies模块
- [x] 1.2.1 创建 `cas-dependencies/pom.xml`
- [x] 1.2.2 设置packaging为pom
- [x] 1.2.3 在properties中定义所有第三方依赖版本
- [x] 1.2.4 在dependencyManagement中统一管理依赖版本
- [x] 1.2.5 包含Spring Boot、MyBatis-Plus、Redis、RabbitMQ等

---

### 阶段2：创建cas-framework框架层

#### 2.1 创建cas-common公共模块
- [x] 2.1.1 创建 `cas-framework/cas-common/pom.xml`
- [x] 2.1.2 依赖cas-dependencies
- [x] 2.1.3 创建DDD基础包结构

##### 2.1.4 迁移entity实体类到domain层
- [x] 2.1.4.1 创建 `com.laoliu.cas.common.domain.entity` 包
- [x] 2.1.4.2 迁移 User.java
- [x] 2.1.4.3 迁移 Services.java
- [x] 2.1.4.4 迁移 Item.java
- [x] 2.1.4.5 迁移 FileEntity.java
- [x] 2.1.4.6 迁移 AiChatHistory.java

##### 2.1.5 迁移enums枚举类
- [x] 2.1.5.1 创建 `com.laoliu.cas.common.enums` 包
- [x] 2.1.5.2 迁移 UserRoleEnum.java
- [x] 2.1.5.3 迁移 CodeGenerator.java
- [x] 2.1.5.4 迁移 ManageStatus.java
- [x] 2.1.5.5 迁移 ServiceStatus.java

##### 2.1.6 迁移exception异常类
- [x] 2.1.6.1 创建 `com.laoliu.cas.common.exception` 包
- [x] 2.1.6.2 迁移 BusinessException.java
- [x] 2.1.6.3 迁移 ForbiddenException.java
- [x] 2.1.6.4 迁移 UnauthorizedException.java
- [x] 2.1.6.5 迁移 ResourceNotFoundException.java
- [x] 2.1.6.6 创建 `com.laoliu.cas.common.exception.code` 包
- [x] 2.1.6.7 迁移 ErrorCode.java
- [x] 2.1.6.8 迁移 LoginErrorCode.java
- [x] 2.1.6.9 迁移 UserErrorCode.java
- [x] 2.1.6.10 迁移 ServiceErrorCode.java
- [x] 2.1.6.11 迁移 RoleErrorCode.java
- [x] 2.1.6.12 迁移 EmailErrorCode.java
- [x] 2.1.6.13 迁移 ServiceStatusErrorCode.java

##### 2.1.7 迁移通用result响应
- [x] 2.1.7.1 创建 `com.laoliu.cas.common.result` 包
- [x] 2.1.7.2 迁移 CommonResult.java

##### 2.1.8 迁移annotation注解
- [x] 2.1.8.1 创建 `com.laoliu.cas.common.annotation` 包
- [x] 2.1.8.2 迁移 RequireRole.java

##### 2.1.9 迁移工具类
- [x] 2.1.9.1 创建 `com.laoliu.cas.common.util` 包
- [x] 2.1.9.2 迁移 PasswordUtils.java（不依赖Spring）

##### 2.1.10 验证cas-common模块
- [x] 2.1.10.1 运行 `mvn clean compile` 验证编译

#### 2.2 创建cas-spring-boot-starter-web模块
- [x] 2.2.1 创建 `cas-framework/cas-spring-boot-starter-web/pom.xml`
- [x] 2.2.2 依赖cas-common和spring-boot-starter-web
- [x] 2.2.3 创建 `com.laoliu.cas.starter.web.config` 包
- [x] 2.2.4 迁移 WebMvcConfig.java
- [x] 2.2.5 迁移 SecurityConfig.java（CORS配置）
- [x] 2.2.6 创建全局异常处理器相关类
- [x] 2.2.7 创建自动配置文件 META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports

#### 2.3 创建cas-spring-boot-starter-mybatis模块
- [x] 2.3.1 创建 `cas-framework/cas-spring-boot-starter-mybatis/pom.xml`
- [x] 2.3.2 依赖cas-common和mybatis-plus-spring-boot3-starter
- [x] 2.3.3 创建MyBatisPlusConfig配置类
- [x] 2.3.4 配置分页插件
- [x] 2.3.5 配置字段自动填充

#### 2.4 创建cas-spring-boot-starter-redis模块
- [x] 2.4.1 创建 `cas-framework/cas-spring-boot-starter-redis/pom.xml`
- [x] 2.4.2 依赖cas-common和spring-boot-starter-data-redis
- [x] 2.4.3 创建 `com.laoliu.cas.starter.redis.config` 包
- [x] 2.4.4 迁移 RedisConfig.java
- [x] 2.4.5 迁移 RedisUtil.java

#### 2.5 创建cas-spring-boot-starter-mq模块
- [x] 2.5.1 创建 `cas-framework/cas-spring-boot-starter-mq/pom.xml`
- [x] 2.5.2 依赖cas-common和spring-boot-starter-amqp
- [x] 2.5.3 创建 `com.laoliu.cas.starter.mq.config` 包
- [x] 2.5.4 迁移 RabbitMQConfig.java
- [x] 2.5.5 创建EmailMQProducer和Consumer基类

#### 2.6 创建cas-spring-boot-starter-security模块
- [x] 2.6.1 创建 `cas-framework/cas-spring-boot-starter-security/pom.xml`
- [x] 2.6.2 依赖cas-common和jjwt
- [x] 2.6.3 创建 `com.laoliu.cas.starter.security.config` 包
- [x] 2.6.4 迁移 JWTUtils.java
- [x] 2.6.5 创建Token解析工具

#### 2.7 创建cas-spring-boot-starter-test模块
- [x] 2.7.1 创建测试基类

#### 2.8 验证cas-framework模块
- [x] 2.8.1 运行 `mvn clean compile` 验证编译

---

### 阶段3：创建cas-module-infra基础设施模块

#### 3.1 创建DDD基础包结构
- [x] 3.1.1 创建 `cas-module-infra/pom.xml`
- [x] 3.1.2 依赖cas-framework各starter模块
- [x] 3.1.3 创建DDD分层包结构

#### 3.2 创建domain层
- [x] 3.2.1 创建 `com.laoliu.cas.infra.domain` 包（空包，仅定义接口）
- [x] 3.2.2 定义仓储接口（如FileRepository.java）

#### 3.3 创建infrastructure层
- [x] 3.3.1 创建 `com.laoliu.cas.infra.infrastructure.persistence` 包
- [x] 3.3.2 迁移mapper接口（UserMapper.java等）
- [x] 3.3.3 创建MyBatis XML映射文件

##### 3.3.4 迁移config配置类
- [x] 3.3.4.1 创建 `com.laoliu.cas.infra.infrastructure.config` 包
- [x] 3.3.4.2 迁移 QwenConfig.java
- [x] 3.3.4.3 迁移 DeepSeekConfig.java
- [x] 3.3.4.4 迁移 OSSConfig.java
- [x] 3.3.4.5 迁移 RestTemplateConfig.java

##### 3.3.5 迁移mq消息队列
- [x] 3.3.5.1 创建 `com.laoliu.cas.infra.infrastructure.mq` 包
- [x] 3.3.5.2 迁移 EmailMQProducer.java
- [x] 3.3.5.3 迁移 EmailMQConsumer.java

##### 3.3.6 迁移service服务
- [x] 3.3.6.1 创建 `com.laoliu.cas.infra.infrastructure.service` 包
- [x] 3.3.6.2 迁移 EmailSendService.java接口
- [x] 3.3.6.3 迁移 EmailServiceImpl.java

#### 3.4 创建api层（跨模块API）
- [x] 3.4.1 创建 `com.laoliu.cas.infra.api` 包
- [x] 3.4.2 创建 GetUserIdViaTokenApi.java 接口
- [x] 3.4.3 创建 GetUserIdViaTokenApiImpl.java 实现

#### 3.5 创建AGENTS.md和README.md
- [x] 3.5.1 创建 `cas-module-infra/AGENTS.md`
- [x] 3.5.2 创建 `cas-module-infra/README.md`

#### 3.6 验证cas-module-infra模块
- [x] 3.6.1 运行 `mvn clean compile` 验证编译

---

### 阶段4：创建cas-module-system系统管理模块

#### 4.1 创建DDD基础包结构
- [ ] 4.1.1 创建 `cas-module-system/pom.xml`
- [ ] 4.1.2 依赖cas-module-infra和cas-thirdparty-aliyun
- [ ] 4.1.3 创建DDD分层包结构

#### 4.2 创建domain层（核心业务逻辑）
- [ ] 4.2.1 创建 `com.laoliu.cas.system.domain.entity` 包
- [ ] 4.2.2 创建 UserEntity.java（用户领域实体）
- [ ] 4.2.3 创建 ServiceEntity.java（服务领域实体）
- [ ] 4.2.4 定义仓储接口

#### 4.3 创建application层（用例编排）
- [ ] 4.3.1 创建 `com.laoliu.cas.system.application.service` 包
- [ ] 4.3.2 创建 UserApplicationService.java
- [ ] 4.3.3 创建 RoleApplicationService.java

##### 4.3.4 迁移service服务接口
- [ ] 4.3.4.1 迁移 UserService.java
- [ ] 4.3.4.2 迁移 RoleService.java

##### 4.3.5 迁移service服务实现
- [ ] 4.3.5.1 迁移 UserServiceImpl.java
- [ ] 4.3.5.2 迁移 RoleServiceImpl.java

#### 4.4 创建interfaces层（接口层）
- [ ] 4.4.1 创建 `com.laoliu.cas.system.interfaces.controller` 包
- [ ] 4.4.2 迁移 LoginController.java
- [ ] 4.4.3 迁移 RegisterController.java
- [ ] 4.4.4 迁移 UserController.java
- [ ] 4.4.5 迁移 RoleController.java
- [ ] 4.4.6 迁移 EmailController.java

##### 4.4.7 创建dto包
- [ ] 4.4.7.1 创建 `com.laoliu.cas.system.interfaces.dto.request` 包
- [ ] 4.4.7.2 迁移 UserLoginRequest.java
- [ ] 4.4.7.3 迁移 UserRegisterRequest.java
- [ ] 4.4.7.4 迁移 ResetPasswordRequest.java
- [ ] 4.4.7.5 迁移 AdminCreateUserRequest.java
- [ ] 4.4.7.6 迁移 EmailRequest.java
- [ ] 4.4.7.7 创建 `com.laoliu.cas.system.interfaces.dto.response` 包
- [ ] 4.4.7.8 迁移 UserResponse.java
- [ ] 4.4.7.9 迁移 EmailResponse.java

##### 4.4.8 迁移converter转换器
- [ ] 4.4.8.1 创建 `com.laoliu.cas.system.interfaces.assembler` 包
- [ ] 4.4.8.2 迁移 UserConverter.java

#### 4.5 创建infrastructure层
- [ ] 4.5.1 创建 `com.laoliu.cas.system.infrastructure.persistence` 包
- [ ] 4.5.2 依赖注入UserMapper等

##### 4.5.3 迁移aspect切面
- [ ] 4.5.3.1 创建 `com.laoliu.cas.system.infrastructure.aspect` 包
- [ ] 4.5.3.2 迁移 RoleAspect.java

##### 4.5.4 迁移exception异常处理
- [ ] 4.5.4.1 创建 `com.laoliu.cas.system.infrastructure.exception` 包
- [ ] 4.5.4.2 迁移 GlobalExceptionHandler.java

#### 4.6 创建api层（跨模块API）
- [ ] 4.6.1 创建 `com.laoliu.cas.system.api` 包
- [ ] 4.6.2 定义系统管理相关API接口

#### 4.7 创建AGENTS.md和README.md
- [ ] 4.7.1 创建 `cas-module-system/AGENTS.md`
- [ ] 4.7.2 创建 `cas-module-system/README.md`

#### 4.8 验证cas-module-system模块
- [ ] 4.8.1 运行 `mvn clean compile` 验证编译

---

### 阶段5：创建cas-module-appointment预约业务模块

#### 5.1 创建DDD基础包结构
- [ ] 5.1.1 创建 `cas-module-appointment/pom.xml`
- [ ] 5.1.2 依赖cas-module-system和cas-module-infra
- [ ] 5.1.3 创建DDD分层包结构

#### 5.2 创建domain层（核心业务逻辑）
- [ ] 5.2.1 创建 `com.laoliu.cas.appointment.domain.entity` 包
- [ ] 5.2.2 创建 BookingEntity.java（预约领域实体）
- [ ] 5.2.3 创建 `com.laoliu.cas.appointment.domain.repository` 包
- [ ] 5.2.4 定义 BookingRepository.java 仓储接口

#### 5.3 创建application层（用例编排）
- [ ] 5.3.1 创建 `com.laoliu.cas.appointment.application.service` 包
- [ ] 5.3.2 创建 BookingApplicationService.java

##### 5.3.3 迁移service服务
- [ ] 5.3.3.1 迁移 BookService.java
- [ ] 5.3.3.2 迁移 BookServiceImpl.java
- [ ] 5.3.3.3 迁移 ServiceController相关服务

#### 5.4 创建interfaces层（接口层）
- [ ] 5.4.1 创建 `com.laoliu.cas.appointment.interfaces.controller` 包
- [ ] 5.4.2 迁移 BookController.java
- [ ] 5.4.3 迁移 ServiceController.java
- [ ] 5.4.4 迁移 ServiceStatusController.java

##### 5.4.5 创建dto包
- [ ] 5.4.5.1 创建 `com.laoliu.cas.appointment.interfaces.dto.request` 包
- [ ] 5.4.5.2 迁移 ServiceAddRequest.java
- [ ] 5.4.5.3 迁移 AuditRequest.java
- [ ] 5.4.5.4 创建 `com.laoliu.cas.appointment.interfaces.dto.response` 包
- [ ] 5.4.5.5 迁移 BookResultResponse.java
- [ ] 5.4.5.6 迁移 ServiceStatusResponse.java
- [ ] 5.4.5.7 迁移 ServicesRespVO.java
- [ ] 5.4.5.8 迁移 UserInfoAndServicesViaMPRespVO.java

#### 5.5 创建infrastructure层
- [ ] 5.5.1 创建 `com.laoliu.cas.appointment.infrastructure.persistence.repository` 包
- [ ] 5.5.2 实现 BookingRepository.java
- [ ] 5.5.3 迁移 ItemMapper.java

#### 5.6 创建api层（跨模块API）
- [ ] 5.6.1 创建 `com.laoliu.cas.appointment.api` 包

#### 5.7 创建AGENTS.md和README.md
- [ ] 5.7.1 创建 `cas-module-appointment/AGENTS.md`
- [ ] 5.7.2 创建 `cas-module-appointment/README.md`

#### 5.8 验证cas-module-appointment模块
- [ ] 5.8.1 运行 `mvn clean compile` 验证编译

---

### 阶段6：创建cas-thirdparty-aliyun第三方集成模块

#### 6.1 创建模块结构
- [ ] 6.1.1 创建 `cas-thirdparty-aliyun/pom.xml`
- [ ] 6.1.2 只依赖cas-common，保持轻量
- [ ] 6.1.3 创建DDD包结构

#### 6.2 创建domain层（定义接口）
- [ ] 6.2.1 创建 `com.laoliu.cas.thirdparty.aliyun.domain` 包
- [ ] 6.2.2 定义 OssApi.java 接口
- [ ] 6.2.3 定义 QwenApi.java 接口

#### 6.3 创建infrastructure层（实现）
- [ ] 6.3.1 创建 `com.laoliu.cas.thirdparty.aliyun.infrastructure` 包

##### 6.3.2 迁移AI相关服务
- [ ] 6.3.2.1 创建 `com.laoliu.cas.thirdparty.aliyun.infrastructure.ai` 包
- [ ] 6.3.2.2 迁移 CallModelService.java
- [ ] 6.3.2.3 迁移 CallModelServiceImpl.java
- [ ] 6.3.2.4 迁移 AiChatHistoryMapper.java
- [ ] 6.3.2.5 迁移相关VO（ChatReqVO.java, ChatRespVO.java）

##### 6.3.3 迁移文件存储服务
- [ ] 6.3.3.1 创建 `com.laoliu.cas.thirdparty.aliyun.infrastructure.oss` 包
- [ ] 6.3.3.2 迁移 OSSService.java
- [ ] 6.3.3.3 迁移 OSSServiceImpl.java
- [ ] 6.3.3.4 迁移 FileService.java
- [ ] 6.3.3.5 迁移 FileServiceImpl.java
- [ ] 6.3.3.6 迁移 FileMapper.java
- [ ] 6.3.3.7 迁移相关VO

#### 6.4 创建interfaces层
- [ ] 6.4.1 创建 `com.laoliu.cas.thirdparty.aliyun.interfaces.controller` 包
- [ ] 6.4.2 迁移 CallTheModelController.java
- [ ] 6.4.3 迁移 OSSController.java
- [ ] 6.4.4 迁移 FileController.java

#### 6.5 创建AGENTS.md和README.md
- [ ] 6.5.1 创建 `cas-thirdparty-aliyun/AGENTS.md`
- [ ] 6.5.2 创建 `cas-thirdparty-aliyun/README.md`

#### 6.6 验证cas-thirdparty-aliyun模块
- [ ] 6.6.1 运行 `mvn clean compile` 验证编译

---

### 阶段7：创建cas-server启动入口模块

#### 7.1 创建模块结构
- [ ] 7.1.1 创建 `cas-server/pom.xml`
- [ ] 7.1.2 依赖所有业务模块
- [ ] 7.1.3 配置spring-boot-maven-plugin

#### 7.2 迁移启动类
- [ ] 7.2.1 创建 `com.laoliu.cas` 包
- [ ] 7.2.2 迁移 CampusAppointmentSystemApplication.java
- [ ] 7.2.3 修改@MapperScan扫描路径

#### 7.3 迁移配置文件
- [ ] 7.3.1 创建 `cas-server/src/main/resources/` 目录
- [ ] 7.3.2 迁移 application.properties
- [ ] 7.3.3 迁移 application.yaml
- [ ] 7.3.4 迁移 banner.txt
- [ ] 7.3.5 迁移 logback-spring.xml
- [ ] 7.3.6 迁移 163_Email.yaml
- [ ] 7.3.7 迁移 QQ_Email.yaml

#### 7.4 迁移静态资源
- [ ] 7.4.1 迁移 temple/ 目录
- [ ] 7.4.2 迁移 uploads/ 目录（如存在）

#### 7.5 验证cas-server模块
- [ ] 7.5.1 运行 `mvn clean compile` 验证编译

---

### 阶段8：最终验证
- [x] 8.1 验证所有模块目录结构正确
- [x] 8.2 验证pom.xml依赖关系正确
- [x] 8.3 验证DDD分层结构正确

---

#### 8.4 编译整个项目
- [ ] 8.4.1 在根目录运行 `mvn clean install -DskipTests`
- [ ] 8.4.2 确保所有模块编译通过

#### 8.5 启动项目测试
- [ ] 8.5.1 启动cas-server模块
- [ ] 8.5.2 验证数据库连接正常
- [ ] 8.5.3 验证Redis连接正常
- [ ] 8.5.4 验证RabbitMQ连接正常

#### 8.6 功能测试
- [ ] 8.6.1 测试登录接口
- [ ] 8.6.2 测试注册接口
- [ ] 8.6.3 测试服务查询接口
- [ ] 8.6.4 测试预约接口
- [ ] 8.6.5 测试AI对话接口

#### 8.7 清理原项目文件
- [ ] 8.7.1 确认新项目正常工作后，删除原src目录
- [ ] 8.7.2 保留sql目录（数据库脚本）
- [ ] 8.7.3 保留data目录（Redis数据）
- [ ] 8.7.4 保留interact目录（项目文档）

---

## 五、DDD分层说明

### 5.1 各层职责

| 层级 | 包名 | 职责 | 依赖方向 |
|------|------|------|----------|
| 接口层 | interfaces | 接收请求、参数校验、调用应用层、返回响应 | → application |
| 应用层 | application | 编排领域对象、协调业务流程、事务控制 | → domain |
| 领域层 | domain | 核心业务逻辑、实体、值对象、领域服务、仓储接口 | 无外部依赖 |
| 基础设施层 | infrastructure | 仓储实现、外部服务调用、消息队列、技术配置 | → domain（实现仓储接口） |
| API层 | api | 跨模块API接口定义和实现 | → application |

### 5.2 DDD关键原则

1. **领域纯度**：领域层不依赖任何外部框架，纯业务逻辑，可独立测试
2. **依赖倒置**：基础设施层实现领域层定义的仓储接口
3. **单向依赖**：上层依赖下层，禁止循环依赖
4. **接口隔离**：模块间通过api/接口调用，不直接调用Service

---

## 六、依赖关系图

```
┌─────────────────────────────────────────────────────────────────┐
│                         cas-server                              │
│                        (启动入口模块)                              │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                      业务模块层                                   │
│  ┌─────────────────────┐    ┌─────────────────────┐           │
│  │cas-module-system   │    │cas-module-appointment│           │
│  │   (系统管理)         │    │    (预约业务)         │           │
│  └─────────┬───────────┘    └──────────┬──────────┘           │
└────────────┼────────────────────────────┼──────────────────────┘
             │                            │
             ▼                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                      基础设施层                                   │
│  ┌─────────────────────┐    ┌─────────────────────┐           │
│  │  cas-module-infra   │    │cas-thirdparty-aliyun│           │
│  │   (基础设施)         │    │   (第三方集成)        │           │
│  └─────────┬───────────┘    └──────────┬──────────┘           │
└────────────┼────────────────────────────┼──────────────────────┘
             │                            │
             └──────────────┬─────────────┘
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                      框架层                                      │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌────────┐ │
│  │ cas-     │ │ starter- │ │ starter- │ │ starter- │ │starter-│ │
│  │ common   │ │ web      │ │ mybatis  │ │ redis    │ │  mq   │ │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘ └────────┘ │
└─────────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                    cas-dependencies                              │
│                      (BOM版本管理)                               │
└─────────────────────────────────────────────────────────────────┘
```

---

## 七、注意事项

### 7.1 包名变更对照
| 原包名 | 新包名 |
|--------|--------|
| com.laoliu.system | com.laoliu.cas.system |
| com.laoliu.system.entity | com.laoliu.cas.common.domain.entity |
| com.laoliu.system.controller | com.laoliu.cas.xxx.interfaces.controller |
| com.laoliu.system.service | com.laoliu.cas.xxx.application.service |
| com.laoliu.system.mapper | com.laoliu.cas.xxx.infrastructure.persistence.mapper |

### 7.2 模块依赖规则
- 只能上层依赖下层
- 禁止循环依赖
- 模块间通过api/接口通信
- 使用@Lazy防止循环依赖

### 7.3 编译顺序
1. cas-dependencies
2. cas-framework/cas-common
3. cas-framework/xxx-starter
4. cas-module-infra
5. cas-thirdparty-aliyun
6. cas-module-system
7. cas-module-appointment
8. cas-server

---

## 八、预期项目结构

```
CampusAppointmentSystem/
├── pom.xml
├── cas-dependencies/
│   └── pom.xml
├── cas-framework/
│   ├── pom.xml
│   ├── cas-common/
│   │   └── pom.xml
│   ├── cas-spring-boot-starter-web/
│   │   └── pom.xml
│   ├── cas-spring-boot-starter-mybatis/
│   │   └── pom.xml
│   ├── cas-spring-boot-starter-redis/
│   │   └── pom.xml
│   ├── cas-spring-boot-starter-mq/
│   │   └── pom.xml
│   └── cas-spring-boot-starter-security/
│       └── pom.xml
├── cas-module-infra/
│   ├── pom.xml
│   ├── src/main/java/com/laoliu/cas/infra/
│   ├── AGENTS.md
│   └── README.md
├── cas-module-system/
│   ├── pom.xml
│   ├── src/main/java/com/laoliu/cas/system/
│   ├── AGENTS.md
│   └── README.md
├── cas-module-appointment/
│   ├── pom.xml
│   ├── src/main/java/com/laoliu/cas/appointment/
│   ├── AGENTS.md
│   └── README.md
├── cas-thirdparty-aliyun/
│   ├── pom.xml
│   ├── src/main/java/com/laoliu/cas/thirdparty/aliyun/
│   ├── AGENTS.md
│   └── README.md
├── cas-server/
│   ├── pom.xml
│   └── src/main/java/com/laoliu/cas/
├── sql/
├── data/
└── interact/
```

---

*计划制定时间：2026-05-28*
