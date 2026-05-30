# Campus Appointment System 项目结构与代码总结

## 一、项目概述

### 1.1 项目基本信息
- **项目名称**: CampusAppointmentSystem (CAS)
- **版本**: 0.0.1-SNAPSHOT
- **Java版本**: 17
- **Spring Boot版本**: 3.5.10
- **包名**: com.laoliu.system
- **描述**: 校园预约系统 - 一个提供校园服务预约功能的Web应用

### 1.2 核心技术栈

| 技术类别 | 技术名称 | 版本 | 用途 |
|---------|---------|------|------|
| 框架 | Spring Boot | 3.5.10 | 核心框架 |
| 框架 | Spring Security | 3.5.13 | 安全认证 |
| ORM | MyBatis-Plus | 3.5.15 | 数据库操作 |
| 数据库 | MySQL | - | 关系型数据库 |
| 缓存 | Redis | - | 缓存/会话存储 |
| 消息队列 | RabbitMQ | - | 异步邮件发送 |
| API文档 | Swagger/OpenAPI | - | 接口文档 |
| API增强 | Knife4j | 4.4.0 | API增强文档 |
| JWT | jjwt | 0.12.6 | Token认证 |
| 云存储 | 阿里云OSS | 3.16.1 | 文件存储 |
| 工具库 | Hutool | 5.8.41 | 工具类集合 |
| 二维码 | Google ZXing | 3.5.0 | 二维码生成 |
| AI模型 | 通义千问(DashScope) | - | AI对话功能 |

---

## 二、项目目录结构

```
CampusAppointmentSystem/
├── data/                           # 数据目录
│   └── redis/                      # Redis持久化数据
│       ├── dump.rdb
│       └── appendonlydir/
├── sql/                            # SQL脚本目录
│   ├── database.sql               # 数据库创建脚本
│   ├── user.sql                  # 用户表结构
│   ├── services.sql              # 服务表结构
│   ├── item.sql                  # 预约订单表结构
│   ├── ai_chat_history.sql       # AI对话历史表
│   ├── file.sql                  # 文件表(预留)
│   └── data.sql                  # 测试数据
├── src/
│   ├── main/
│   │   ├── java/com/laoliu/system/
│   │   │   ├── annotation/       # 自定义注解
│   │   │   ├── api/              # API接口定义
│   │   │   ├── aspect/           # AOP切面
│   │   │   ├── common/           # 公共组件
│   │   │   ├── config/           # 配置类
│   │   │   ├── controller/       # 控制器层
│   │   │   ├── converter/        # 对象转换器
│   │   │   ├── entity/           # 实体类
│   │   │   ├── enums/            # 枚举类
│   │   │   ├── exception/        # 异常处理
│   │   │   ├── mapper/           # 数据访问层
│   │   │   ├── mq/               # 消息队列
│   │   │   ├── service/          # 业务逻辑层
│   │   │   ├── utils/            # 工具类
│   │   │   └── vo/               # 视图对象
│   │   └── resources/
│   │       ├── com/laoliu/system/mapper/  # MyBatis XML映射文件
│   │       ├── mapper/                    # 其他Mapper XML
│   │       ├── application.yaml          # 应用配置
│   │       ├── application.properties    # 应用属性
│   │       ├── logback-spring.xml        # 日志配置
│   │       └── temple/                   # 模板文件
│   └── test/                     # 测试目录
├── pom.xml                       # Maven配置
└── summary.md                    # 本文档
```

---

## 三、数据库设计

### 3.1 数据库: cas_db (字符集: utf8mb4_unicode_ci)

#### 3.1.1 用户表 (user)
```sql
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,     -- 主键
    name VARCHAR(64) COMMENT '用户名',
    grade VARCHAR(32) COMMENT '年级',
    sex VARCHAR(8) COMMENT '性别',
    age TINYINT UNSIGNED COMMENT '年龄',
    email VARCHAR(128) NOT NULL UNIQUE COMMENT '邮箱',
    password VARCHAR(255) NOT NULL COMMENT '密码(BCrypt加密)',
    role INT DEFAULT 0 COMMENT '角色: 0-普通用户, 1-管理员, 2-超级管理员'
);
```

#### 3.1.2 服务表 (services)
```sql
CREATE TABLE services (
    service_id INT AUTO_INCREMENT PRIMARY KEY,
    service_name VARCHAR(20) COMMENT '服务名称',
    service_describe VARCHAR(100) COMMENT '服务描述',
    service_state TINYINT(1) DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### 3.1.3 预约订单表 (item)
```sql
CREATE TABLE item (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    service_id INT NOT NULL,
    manage_status INT DEFAULT 0 COMMENT '状态: 0-待审核, 1-通过, 2-拒绝, 3-取消',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES services(service_id) ON DELETE CASCADE
);
```

#### 3.1.4 AI对话历史表 (ai_chat_history)
```sql
CREATE TABLE ai_chat_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    model VARCHAR(64) NOT NULL COMMENT '模型名称',
    user_message TEXT NOT NULL,
    ai_response TEXT NOT NULL,
    response_time_ms INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

---

## 四、实体类设计 (Entity)

| 实体类 | 表名 | 说明 |
|-------|------|------|
| User | user | 用户实体，包含id/name/grade/sex/age/email/password/role |
| Services | services | 服务实体，包含serviceId/serviceName/serviceDescribe/serviceState |
| Item | item | 预约订单实体，包含orderId/userId/serviceId/createTime/updateTime/manageStatus |
| AiChatHistory | ai_chat_history | AI对话历史，包含id/userId/model/userMessage/aiResponse/responseTimeMs/createdAt/updatedAt |
| FileEntity | file_info | 文件实体(预留) |

### 实体类特点
- 使用 `@Component` 或无注解（MyBatis-Plus自动处理）
- 使用 `@TableName` 指定数据库表名
- 使用 `@TableId` 配置主键策略（AUTO自增）
- 部分使用 Lombok `@Data` 简化代码

---

## 五、数据访问层 (Mapper)

### 5.1 Mapper接口

| Mapper | 继承 | 功能 |
|-------|------|------|
| UserMapper | BaseMapper<User> | 用户CRUD + 自定义查询 |
| ServiceMapper | BaseMapper<Services> | 服务CRUD |
| ItemMapper | BaseMapper<Item> | 预约订单CRUD + 复杂查询 |
| AiChatHistoryMapper | BaseMapper<AiChatHistory> | AI对话历史CRUD |
| FileMapper | BaseMapper<FileEntity> | 文件CRUD(预留) |

### 5.2 自定义SQL操作

**UserMapper自定义方法:**
- `getRoleByUserId(Long userId)` - 获取用户角色
- `updateRoleToCommonUser(Long userId)` - 降级为普通用户
- `updateRoleToAdmin(Long userId)` - 升级为管理员
- `getAllBookings(Long userId)` - 获取用户所有预约
- `getEncodePasswordByEmail(String email)` - 通过邮箱获取加密密码
- `getUserIdByEmail(String email)` - 通过邮箱获取用户ID
- `getAllUsers()` - 获取所有用户
- `updatePasswordByEmail(String email, String password)` - 通过邮箱更新密码

**ItemMapper自定义方法:**
- `selectUserServices(Long userId)` - 获取用户预约的服务
- `getServiceStatus()` - 获取所有服务状态(管理员)
- `getServiceStatusByUserId(Long userId)` - 获取用户自己的服务状态
- `insertServices(userId, serviceIds)` - 批量插入预约
- `setBookingStatus(bookingId, userId)` - 取消预约
- `setBookingStatusByParts(userId, bookingIds)` - 批量取消预约

### 5.3 Mapper XML位置
- `resources/com/laoliu/system/mapper/UserMapper.xml`
- `resources/com/laoliu/system/mapper/ServiceMapper.xml`
- `resources/com/laoliu/system/mapper/ItemMapper.xml`
- `resources/mapper/AiChatHistoryMapper.xml`

---

## 六、业务逻辑层 (Service)

### 6.1 Service接口与实现

| Service接口 | 实现类 | 功能 |
|------------|--------|------|
| UserService | UserServiceImpl | 用户信息及预约查询 |
| BookService | BookServiceImpl | 预约服务/取消预约 |
| RoleService | RoleServiceImpl | 角色管理 |
| EmailSendService | EmailServiceImpl | 邮件发送 |
| CallModelService | CallModelServiceImpl | AI模型调用 |
| FileService | FileServiceImpl | 文件上传 |
| QRCodeService | QRCodeServiceImpl | 二维码生成 |
| OSSService | OSSServiceImpl | 阿里云OSS操作 |
| IAiChatHistoryService | AiChatHistoryServiceImpl | AI对话历史 |

### 6.2 核心业务逻辑

#### BookServiceImpl - 预约服务
```java
// 预约服务流程:
1. 验证服务ID列表不为空
2. 逐个验证服务是否存在且启用(serviceState == 1)
3. 调用itemMapper.insertServices()批量插入预约记录
4. 返回更新后的用户信息
```

#### UserServiceImpl - 获取用户信息及预约
```java
// 查询流程:
1. 查询用户基本信息
2. 查询用户的预约记录(Item表)
3. 查询关联的服务信息(Services表)
4. 过滤掉已取消的服务(manageStatus != 1)
5. 封装返回UserInfoAndServicesViaMPRespVO
```

### 6.3 事务管理
- `BookServiceImpl.cancelBookings()` 使用 `@Transactional` 保证事务

---

## 七、控制层 (Controller)

### 7.1 Controller一览

| Controller | 路径 | 功能 |
|-----------|------|------|
| UserController | /user | 用户信息管理 |
| LoginController | /login | 用户登录/重置密码 |
| RegisterController | /register | 用户注册 |
| BookController | /book | 预约服务 |
| ServiceController | /service | 服务管理 |
| ServiceStatusController | /service-status | 服务状态/审核 |
| RoleController | /role | 角色管理 |
| EmailController | /email | 邮件发送 |
| CallTheModelController | /callTheLargeModel | AI对话 |
| FileController | /file | 文件上传 |
| QRCodeController | /qrcode | 二维码生成 |
| OSSController | /oss | 阿里云OSS |
| WeatherController | /weather | 天气查询 |
| GraphicVerificationController | /graphic | 图形验证码 |
| JwtTestController | /jwt | JWT测试 |
| Hello | /hello | 测试接口 |

### 7.2 主要API接口

#### 认证相关
| 方法 | 路径 | 说明 | 权限 |
|-----|------|------|-----|
| POST | /login | 用户登录 | 公开 |
| POST | /login/reset | 重置密码 | 公开 |
| POST | /register/verify-code | 注册验证 | 公开 |

#### 用户相关
| 方法 | 路径 | 说明 | 权限 |
|-----|------|------|-----|
| GET | /user | 获取当前用户信息 | USER |
| GET | /user/all_users | 获取所有用户 | ADMIN |
| POST | /user/create | 创建用户 | SUPER_ADMIN |
| GET | /user/get_all_bookings | 获取用户所有预约 | USER |

#### 预约相关
| 方法 | 路径 | 说明 | 权限 |
|-----|------|------|-----|
| POST | /book | 预约服务 | USER |
| GET | /book/allService | 查看所有预约 | USER |
| POST | /book/cancel | 取消预约 | USER |

#### 服务管理
| 方法 | 路径 | 说明 | 权限 |
|-----|------|------|-----|
| GET | /service | 获取所有服务 | 公开 |
| POST | /service | 添加服务 | ADMIN |
| GET | /service/id | 获取指定用户服务 | ADMIN |

#### 服务状态
| 方法 | 路径 | 说明 | 权限 |
|-----|------|------|-----|
| GET | /service-status | 获取所有服务状态 | ADMIN |
| GET | /service-status/user | 获取用户自己的服务状态 | USER |
| POST | /service-status/audit | 审核服务预约 | ADMIN |

#### AI对话
| 方法 | 路径 | 说明 | 权限 |
|-----|------|------|-----|
| POST | /callTheLargeModel/callTheModel/qwen | 调用Qwen模型 | USER |

---

## 八、权限控制机制

### 8.1 角色枚举 (UserRoleEnum)
```java
USER(0, "普通用户")      - 基本用户权限
ADMIN(1, "管理员")       - 管理员权限
SUPER_ADMIN(2, "超级管理员") - 最高权限
```

### 8.2 权限注解 (@RequireRole)
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRole {
    UserRoleEnum[] value() default {UserRoleEnum.USER};
}
```

### 8.3 权限切面 (RoleAspect)
- 使用 `@Around` 环绕通知拦截带有 `@RequireRole` 注解的方法
- 从请求Header获取Authorization Token
- 解析Token获取userId
- 查询用户角色
- 使用 `UserRoleEnum.hasPermission()` 判断是否有权限
- 无权限时返回JSON错误响应

### 8.4 权限判断逻辑
```java
// 用户角色码 >= 需要的角色码 则有权限
// ADMIN(1) >= USER(0) ✓
// SUPER_ADMIN(2) >= ADMIN(1) ✓
```

---

## 九、安全机制

### 9.1 密码安全
- 使用 `BCryptPasswordEncoder` 加密
- 密码不会明文存储或传输

### 9.2 JWT认证
- 使用 `HS512` 算法签名
- Token包含 userId、iat、exp
- 可配置过期时间 (jwt.expiration)

### 9.3 CORS配置
- 允许所有来源 (`*`)
- 允许 GET/POST/PUT/DELETE/OPTIONS 方法
- 允许所有Header
- 支持凭证

### 9.4 全局安全配置 (SecurityConfig)
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // 禁用CSRF
    // 配置CORS
    // 所有请求都允许访问 (permissive)
}
```

---

## 十、异常处理机制

### 10.1 异常类层次
```
RuntimeException
├── BusinessException (基类)
│   ├── UnauthorizedException (401)
│   ├── ForbiddenException (403)
│   └── ResourceNotFoundException (404)
```

### 10.2 错误码体系

| 错误码范围 | 类别 |
|----------|------|
| 1000-1099 | 用户相关错误 |
| 1100-1199 | 认证授权相关 |
| 1200-1299 | 验证码相关 |
| 1300-1399 | 预约相关 |
| 1400-1499 | 文件相关 |
| 1500-1599 | 系统相关 |

### 10.3 全局异常处理器 (GlobalExceptionHandler)
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 处理NoResourceFoundException - 404
    // 处理Exception - 500
    // 处理RuntimeException - 500
    // 处理MethodArgumentNotValidException - 400 (参数校验)
    // 处理BindException - 400
    // 处理BusinessException - 根据错误码
    // 处理UnauthorizedException - 401
    // 处理ForbiddenException - 403
    // 处理ResourceNotFoundException - 404
    // 处理IllegalArgumentException - 400
}
```

---

## 十一、消息队列 (RabbitMQ)

### 11.1 配置
```java
public class RabbitMQConfig {
    EMAIL_EXCHANGE = "email.exchange"
    EMAIL_QUEUE = "email.queue"
    EMAIL_ROUTING_KEY = "email.send"
}
```

### 11.2 生产者 (EmailMQProducer)
```java
@Component
public class EmailMQProducer {
    public void sendEmailTask(String to, String subject, String content) {
        // 发送消息到 email.exchange
    }
}
```

### 11.3 消费者 (EmailMQConsumer)
```java
@Component
@RabbitListener(queues = "email.queue")
public class EmailMQConsumer {
    public void handleEmailMessage(Map<String, String> message) {
        // 接收消息并调用 EmailSendService 发送邮件
    }
}
```

---

## 十二、配置管理

### 12.1 application.yaml 主要配置
```yaml
spring:
  mail:           # 邮件配置 (163邮箱)
  servlet:        # 文件上传配置 (50MB限制)

file:
  upload:
    path: ./upload/
    prefix: /api/files/
    server-address: http://localhost:8080

aliyun:
  oss:           # 阿里云OSS配置

qwen:
  api-key:       # 通义千问API Key
  api-url:       # API地址
  timeout: 30000

jwt:
  secret:        # Base64编码的密钥
  expiration:    # 过期时间
```

### 12.2 配置类
| 配置类 | 功能 |
|-------|------|
| SecurityConfig | Spring Security配置 |
| RedisConfig | Redis模板配置 |
| RabbitMQConfig | RabbitMQ队列配置 |
| WebMvcConfig | Web MVC配置 |
| RestTemplateConfig | RestTemplate配置 |
| DeepSeekConfig | DeepSeek AI配置 |
| QwenConfig | 通义千问配置 |
| OSSConfig | 阿里云OSS配置 |

---

## 十三、工具类

| 工具类 | 功能 |
|-------|------|
| JWTUtils | JWT Token生成与解析 |
| PasswordUtils | BCrypt密码加密与验证 |
| RedisUtil | Redis验证码存储与获取 |

---

## 十四、API返回格式

### 14.1 统一响应类 (CommonResult)
```java
@Data
public class CommonResult<T> {
    Integer code;      // 状态码
    String message;    // 消息
    T data;           // 数据
}
```

### 14.2 响应工厂方法
```java
CommonResult.success()           // 成功
CommonResult.success(T data)     // 成功带数据
CommonResult.error(code, msg)   // 错误
CommonResult.badRequest(msg)    // 400错误
CommonResult.unauthorized(msg)  // 401错误
CommonResult.forbidden(msg)     // 403错误
CommonResult.notFound(msg)      // 404错误
CommonResult.internalServerError(msg) // 500错误
```

---

## 十五、视图对象 (VO)

### 15.1 Request VO
| VO类 | 用途 |
|-----|------|
| UserLoginRequest | 登录请求 |
| UserRegisterRequest | 注册请求 |
| ResetPasswordRequest | 重置密码请求 |
| ServiceAddRequest | 添加服务请求 |
| EmailRequest | 邮件发送请求 |
| FileUploadReqVO | 文件上传请求 |
| ChatReqVO | AI对话请求 |
| AuditRequest | 审核请求 |
| VerifyCodeReqVO | 验证码请求 |
| AdminCreateUserRequest | 管理员创建用户请求 |

### 15.2 Response VO
| VO类 | 用途 |
|-----|------|
| UserResponse | 用户信息响应 |
| UserInfoAndServicesViaMPRespVO | 用户及服务响应 |
| ServicesRespVO | 服务信息响应 |
| ServiceStatusResponse | 服务状态响应 |
| BookResultResponse | 预约结果响应 |
| ChatRespVO | AI对话响应 |
| EmailResponse | 邮件响应 |
| WeatherResponse | 天气响应 |

---

## 十六、业务流程

### 16.1 用户注册流程
```
1. 用户访问 /email 发送验证码
2. 验证码存入Redis (5分钟有效期)
3. 用户提交注册信息到 /register/verify-code
4. 验证Redis中的验证码
5. 验证用户不存在
6. BCrypt加密密码
7. 插入用户数据
8. 生成JWT Token返回
```

### 16.2 用户登录流程
```
1. 用户提交 email + password 到 /login
2. 通过email查询加密密码
3. BCrypt验证密码
4. 验证通过生成JWT Token
5. 返回Token给客户端
```

### 16.3 预约服务流程
```
1. 用户选择服务，提交 serviceIds 到 /book
2. 从Token解析userId
3. 验证服务存在且启用
4. 批量插入 item 表记录
5. 返回预约结果和用户信息
```

### 16.4 取消预约流程
```
1. 用户提交 bookingIds 到 /book/cancel
2. 从Token解析userId
3. 验证预约属于当前用户
4. 更新 manage_status 为 3 (取消)
5. 返回结果
```

### 16.5 管理员审核流程
```
1. 管理员获取所有服务状态 /service-status
2. 管理员审核预约 /service-status/audit
3. 更新 manage_status (1-通过, 2-拒绝)
4. 发送邮件通知用户
```

### 16.6 AI对话流程
```
1. 用户发送消息到 /callTheLargeModel/callTheModel/qwen
2. 从Token解析userId
3. 调用通义千问API
4. 保存对话历史到 ai_chat_history
5. 返回AI响应
```

---

## 十七、依赖关系图

```
Controller层
    ↓ 调用
Service层
    ↓ 调用
Mapper层 (MyBatis-Plus)
    ↓ 操作
数据库 (MySQL)
    ↓ 使用
Redis (缓存)
RabbitMQ (异步消息)
外部API (阿里云OSS, 通义千问)
```

---

## 十八、注解使用情况

### 18.1 Spring注解
| 注解 | 使用位置 |
|-----|---------|
| @SpringBootApplication | 启动类 |
| @MapperScan | 启动类 |
| @EnableAsync | 启动类 |
| @RestController | Controller |
| @Controller | (未使用) |
| @Service | Service实现 |
| @Component | 配置类/工具类 |
| @Configuration | 配置类 |
| @Bean | 配置类方法 |
| @Autowired | 构造器注入 |
| @RequiredArgsConstructor | (Lombok) |

### 18.2 MyBatis-Plus注解
| 注解 | 用途 |
|-----|------|
| @TableName | 指定表名 |
| @TableId | 主键策略 |
| @TableLogic | 逻辑删除 |

### 18.3 Swagger/OpenAPI注解
| 注解 | 用途 |
|-----|------|
| @Tag | Controller分组 |
| @Operation | 接口描述 |
| @Schema | 属性描述 |

### 18.4 自定义注解
| 注解 | 用途 |
|-----|------|
| @RequireRole | 方法权限控制 |

---

## 十九、存在的问题与改进建议

### 19.1 性能问题
1. **UserServiceImpl.getUserInfoAndBookings()**: 代码注释提到"接口响应要2.5秒"，存在性能问题，需要优化查询
2. **N+1查询问题**: 批量操作时可能存在N+1查询

### 19.2 安全问题
1. **JWT Secret**: 配置在application.yaml中，应使用环境变量
2. **邮件配置**: 邮箱密码硬编码在配置文件中
3. **API Key**: 阿里云OSS和Qwen API Key硬编码

### 19.3 代码规范
1. **错误码混乱**: ServiceStatusErrorCode中有些错误码很奇怪（如3838438, 404404404）
2. **重复代码**: 多个Controller中有类似的Token解析代码
3. **异常处理**: 部分地方使用RuntimeException而非自定义异常

### 19.4 架构改进
1. **三层架构**: 可考虑引入DTO层
2. **配置分离**: dev/test/prod环境配置
3. **日志规范**: 统一日志格式和级别

---

## 二十、重构检查清单

### 20.1 结构完整性检查
- [ ] 所有Entity类是否存在且字段匹配数据库表
- [ ] 所有Mapper接口是否有对应的XML或注解配置
- [ ] 所有Service接口是否有实现类
- [ ] 所有Controller方法是否正确映射URL
- [ ] 所有VO类是否完整

### 20.2 依赖关系检查
- [ ] Controller依赖Service（通过构造器注入）
- [ ] Service依赖Mapper（通过构造器注入）
- [ ] 配置类正确使用@Bean
- [ ] @MapperScan正确扫描Mapper包

### 20.3 功能完整性检查
- [ ] 用户注册/登录/登出功能
- [ ] 预约服务/取消预约功能
- [ ] 服务管理（添加/查询）
- [ ] 角色权限控制
- [ ] 邮件发送功能
- [ ] AI对话功能
- [ ] 文件上传功能

### 20.4 配置检查
- [ ] application.yaml/database.sql一致性
- [ ] Mapper XML namespace正确
- [ ] Redis/RabbitMQ配置正确
- [ ] JWT secret配置正确

---

## 二十一、数据库与代码对应关系

| 数据库表 | Entity | Mapper | Service | Controller |
|---------|--------|--------|---------|------------|
| user | User | UserMapper | UserService | UserController, LoginController |
| services | Services | ServiceMapper | BookService | ServiceController, BookController |
| item | Item | ItemMapper | BookService | BookController, ServiceStatusController |
| ai_chat_history | AiChatHistory | AiChatHistoryMapper | CallModelService | CallTheModelController |
| file_info | FileEntity | FileMapper | FileService | FileController |

---

*文档生成时间: 2026-05-30*
*项目版本: 0.0.1-SNAPSHOT*
