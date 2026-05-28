# Campus Appointment System 项目结构与业务分析

## 一、项目概述

**Campus Appointment System（校园预约系统）** 是一个基于 Spring Boot 3.5.10 + Java 17 的后端服务系统，提供校园服务和AI智能对话功能。

### 核心技术栈

| 技术 | 用途 |
|------|------|
| Spring Boot 3.5.10 | 核心框架 |
| Spring Web | RESTful API开发 |
| Spring AOP | 权限切面控制 |
| Spring Data JPA/MyBatis | 数据持久层 |
| Spring Security | 安全框架（配置CORS） |
| Spring Mail | 邮件发送 |
| Spring AMQP (RabbitMQ) | 消息队列 |
| Spring Data Redis | 缓存和验证码存储 |
| JWT (jjwt 0.12.6) | 用户认证 |
| MySQL | 关系型数据库 |
| 阿里云OSS | 文件存储 |
| 阿里云通义千问(Qwen) | AI大模型对话 |

---

## 二、项目架构

```
src/main/java/com/laoliu/system/
├── annotation/          # 自定义注解
│   └── RequireRole.java # 角色权限注解
├── api/                 # API接口定义
│   ├── GetUserIdViaTokenApi.java
│   └── GetUserIdViaTokenApiImpl.java
├── aspect/              # AOP切面
│   └── RoleAspect.java  # 角色权限切面
├── common/              # 公共组件
│   ├── enums/           # 枚举类
│   ├── exception/       # 异常定义
│   └── result/          # 统一响应格式
├── config/              # 配置类
│   ├── DeepSeekConfig.java
│   ├── OSSConfig.java
│   ├── QwenConfig.java
│   ├── RabbitMQConfig.java
│   ├── RedisConfig.java
│   ├── RestTemplateConfig.java
│   ├── SecurityConfig.java
│   └── WebMvcConfig.java
├── controller/          # 控制器层
├── converter/           # 对象转换器
├── entity/              # 实体类
├── enums/               # 业务枚举
├── exception/           # 异常处理
├── mapper/              # 数据访问层
├── mq/                  # 消息队列
│   ├── EmailMQConsumer.java
│   └── EmailMQProducer.java
├── service/             # 业务逻辑层
├── utils/               # 工具类
└── vo/                  # 值对象
    ├── request/         # 请求VO
    └── response/        # 响应VO
```

---

## 三、数据库设计

### 1. user 用户表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键，自增 |
| name | VARCHAR(64) | 用户名 |
| grade | VARCHAR(32) | 年级/班级 |
| sex | VARCHAR(8) | 性别 |
| age | TINYINT | 年龄 |
| email | VARCHAR(128) | 邮箱（唯一） |
| password | VARCHAR(255) | 密码（BCrypt加密） |
| role | INT | 角色：0-普通用户，1-管理员，2-超级管理员 |

### 2. services 服务表
| 字段 | 类型 | 说明 |
|------|------|------|
| service_id | INT | 主键，自增 |
| service_name | VARCHAR(20) | 服务名称 |
| service_describe | VARCHAR(100) | 服务描述 |
| service_state | TINYINT(1) | 状态：0-禁用，1-启用 |
| create_time | TIMESTAMP | 创建时间 |
| update_time | TIMESTAMP | 更新时间 |

### 3. item 预约订单表
| 字段 | 类型 | 说明 |
|------|------|------|
| order_id | INT | 主键，自增 |
| user_id | BIGINT | 用户ID（外键） |
| service_id | INT | 服务ID（外键） |
| manage_status | INT | 管理状态：0-待审核，1-通过，2-拒绝，3-取消 |
| create_time | TIMESTAMP | 创建时间 |
| update_time | TIMESTAMP | 更新时间 |

### 4. ai_chat_history AI对话历史表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键，自增 |
| user_id | BIGINT | 用户ID |
| model | VARCHAR(64) | 使用的模型名称 |
| user_message | TEXT | 用户问题 |
| ai_response | TEXT | AI回答 |
| response_time_ms | INT | 响应时间（毫秒） |
| created_at | TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | 更新时间 |

### 5. file_info 文件信息表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键，自增 |
| file_name | VARCHAR(255) | 原文件名 |
| file_path | VARCHAR(500) | 存储路径 |
| file_size | BIGINT | 文件大小 |
| file_type | VARCHAR(100) | MIME类型 |
| file_ext | VARCHAR(20) | 文件扩展名 |
| file_uuid | VARCHAR(64) | 唯一UUID |
| upload_user | BIGINT | 上传用户ID |
| create_time | DATETIME | 上传时间 |
| update_time | DATETIME | 更新时间 |
| is_deleted | INT | 软删除标记：0-正常，1-删除 |

---

## 四、核心业务流程

### 4.1 用户注册与登录流程

```
用户注册流程：
1. 用户提交邮箱 → EmailController.sendEmail() 发送验证码
2. 验证码存入Redis（key: email地址，value: 验证码，过期时间5分钟）
3. 用户提交验证码+密码 → RegisterController.verifyEmailCode()
4. 验证Redis中的验证码
5. 密码BCrypt加密后存入数据库
6. 生成JWT Token返回

用户登录流程：
1. 用户提交邮箱+密码 → LoginController.login()
2. 根据邮箱查询加密密码
3. BCrypt验证密码
4. 验证通过生成JWT Token返回
```

### 4.2 服务预约流程

```
预约服务流程：
1. 用户登录系统
2. 调用 GET /service 获取所有可用服务
3. 用户选择服务，调用 POST /book 提交预约（serviceIds列表）
4. BookServiceImpl.bookService() 处理：
   - 验证服务ID有效性
   - 验证服务状态为启用
   - 批量插入item记录
   - 返回用户信息
5. 管理员调用 GET /service-status 查看所有预约
6. 管理员调用 POST /service-status/audit 审核预约

取消预约流程：
1. 用户调用 GET /book/allService 查看预约
2. 用户调用 POST /book/cancel 取消预约（bookingIds列表）
3. BookServiceImpl.cancelBookings() 将manage_status设为3（取消）
```

### 4.3 AI对话流程

```
AI对话流程：
1. 用户登录系统
2. 调用 POST /callTheLargeModel/callTheModel/qwen
3. CallModelServiceImpl.callQwenModel() 处理：
   - 构建WebClient请求Qwen API
   - 发送用户消息
   - 接收AI响应
   - 保存对话历史到ai_chat_history表
4. 返回ChatRespVO（包含AI回复内容、模型名称、响应时间）
```

### 4.4 邮件发送流程（异步MQ）

```
邮件发送流程：
1. EmailController.sendEmail() 接收请求
2. 生成验证码存入Redis（防重复发送：60秒限制）
3. 将邮件任务发送到RabbitMQ队列
4. EmailMQConsumer消费消息
5. EmailServiceImpl.sendEmail() 异步发送邮件

技术亮点：使用RabbitMQ实现邮件发送异步化，提高接口响应速度
```

---

## 五、权限控制机制

### 5.1 角色枚举 (UserRoleEnum)
```java
USER(0, "普通用户")        - 可访问用户接口
ADMIN(1, "管理员")        - 可访问管理员接口
SUPER_ADMIN(2, "超级管理员") - 可创建管理员
```

### 5.2 权限切面 (RoleAspect)
- 使用 `@RequireRole` 注解标记需要权限的接口
- 切面拦截请求，从JWT Token解析用户ID
- 查询用户角色，判断是否有权限访问
- 无权限返回403 Forbidden

### 5.3 权限层级关系
```
SUPER_ADMIN (code=2) > ADMIN (code=1) > USER (code=0)
权限判断逻辑：userRole.code >= requiredRole.code 即有权限
```

---

## 六、统一响应格式

```java
CommonResult<T> {
    Integer code;    // 状态码：200成功，400参数错误，401未授权，403禁止，404未找到，500服务器错误
    String message;  // 消息
    T data;          // 数据
}
```

---

## 七、API接口汇总

### 认证相关
| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| /login | POST | 用户登录 | 公开 |
| /login/reset | POST | 重置密码 | 公开 |
| /register/verify-code | POST | 注册验证 | 公开 |
| /email | POST | 发送邮件 | 公开 |

### 用户相关
| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| /user | GET | 获取用户信息 | 用户 |
| /user/all_users | GET | 获取所有用户 | 管理员 |
| /user/create | POST | 创建用户 | 超级管理员 |
| /user/get_all_bookings | GET | 用户查看预约 | 用户 |

### 服务相关
| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| /service | GET | 获取所有服务 | 公开 |
| /service | POST | 添加服务 | 管理员 |
| /service/id | GET | 获取用户预约服务 | 管理员 |

### 预约相关
| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| /book | POST | 预约服务 | 用户 |
| /book/allService | GET | 查看所有预约 | 用户 |
| /book/cancel | POST | 取消预约 | 用户 |

### 角色相关
| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| /role | GET | 获取用户角色 | 用户 |
| /role | PUT | 修改用户角色 | 管理员 |

### AI对话相关
| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| /callTheLargeModel/callTheModel/qwen | POST | 调用Qwen大模型 | 用户 |

### 服务状态相关
| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| /service-status | GET | 获取所有服务状态 | 管理员 |
| /service-status/user | GET | 获取用户服务状态 | 用户 |
| /service-status/audit | POST | 审核服务预约 | 管理员 |

---

## 八、技术亮点

### 1. 异步消息队列
- 使用RabbitMQ实现邮件发送异步化
- 配置JSON消息转换器
- 解耦邮件发送业务，提高系统响应速度

### 2. 分布式缓存
- 使用Redis存储验证码（防重复发送、过期自动清除）
- 验证码5分钟过期，60秒发送频率限制

### 3. JWT无状态认证
- Token包含用户ID和过期时间
- 支持Token解析和验证
- 支持Token过期检测

### 4. 密码安全
- 使用BCrypt加密存储
- 密码验证使用时间 safe comparison

### 5. 切面编程
- 使用AOP实现权限控制
- 自定义 `@RequireRole` 注解
- 统一异常处理

### 6. AI集成
- 集成阿里云通义千问API
- 支持多种AI模型
- 记录对话历史

---

## 九、配置文件说明

### application.properties 主要配置
```properties
# 数据库
spring.datasource.url=jdbc:mysql://localhost:3306/cas_db

# Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379

# RabbitMQ
spring.rabbitmq.host=127.0.0.1
spring.rabbitmq.port=5672

# JWT
jwt.secret=Base64编码的密钥
jwt.expiration=72000000000（20小时）

# 邮件
email.subject= Campus Appointment System Verification Code
email.code.expiration=300（5分钟）

# 天气API
weather.api.url=https://cn.apihz.cn/api/tianqi/tqyb.php

# 文件上传
file.upload.path=${user.dir}/uploads/
```

---

## 十、业务流程图

### 用户注册流程
```
[开始] → [输入邮箱] → [发送验证码] → [验证码存入Redis]
    ↓
[输入验证码+密码] → [验证Redis验证码] → [密码加密] → [存入数据库]
    ↓
[生成JWT Token] → [返回Token] → [结束]
```

### 服务预约流程
```
[用户登录] → [获取服务列表] → [选择服务] → [提交预约]
    ↓
[验证服务有效性] → [插入预约记录] → [返回预约结果]
    ↓
[管理员审核] → [通过/拒绝] → [发送邮件通知]
```

---

## 十一、待优化点

1. **性能优化**：UserServiceImpl.getUserInfoAndBookings() 查询速度慢（2.5秒+）
2. **注册流程**：管理员注册需要邀请码机制
3. **AI模型**：可扩展支持更多大模型（DeepSeek等）
4. **文件服务**：可增加文件预览、下载功能
5. **日志完善**：增加更详细的业务日志记录

---

*文档生成时间：2026-05-28*
