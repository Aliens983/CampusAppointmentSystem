# Campus Appointment System - 项目架构问题报告

## 概述

经过对重构后项目架构的详细检查，发现以下若干问题需要关注和修复。

---

## 一、模块依赖问题

### 1.1 循环依赖风险 ⚠️ 待处理

**问题描述**：
- `cas-module-appointment` → `cas-module-system`（依赖）
- `cas-module-system` → `cas-module-infra`（依赖）
- `cas-module-system` → `cas-thirdparty-aliyun`（依赖）

**影响分析**：
模块间耦合过紧，业务边界不清晰。appointment模块不应该直接依赖system模块，这违反了领域驱动设计（DDD）的模块划分原则。

**建议**：
- 将跨模块依赖改为通过API接口进行通信（如UserInfoApi模式）
- appointment模块不应该直接依赖system模块，应该通过cas-common中的API接口获取用户信息

**当前状态**：appointment模块通过GetUserIdViaTokenApi获取用户ID，该接口在system模块中。要完全解耦需要较大改动。

---

## 二、数据持久层问题

### 2.1 MyBatis Mapper XML文件位置问题 ✅ 已修复

**修复内容**：
已在各模块创建Mapper XML文件：
- `cas-module-system/src/main/resources/mapper/UserMapper.xml`
- `cas-module-appointment/src/main/resources/mapper/ItemMapper.xml`
- `cas-module-appointment/src/main/resources/mapper/ServiceMapper.xml`

---

### 2.2 ItemDO与Item实体重复问题 ✅ 已修复

**修复内容**：
- 已删除 `cas-common/Item.java`
- 保留 `cas-module-appointment/ItemDO.java` 作为数据访问对象

---

### 2.3 数据库配置不一致 ✅ 已修复

**修复内容**：
- SQL脚本中所有数据库名称已统一为 `cas_db`
- application.yml中数据库URL也改为 `cas_db`
- 涉及文件：database.sql, user.sql, services.sql, item.sql, data.sql, file.sql

---

## 三、消息队列功能问题 ✅ 已修复

### 3.1 RabbitMQ邮件队列功能处理

**修复内容**：
1. 已删除 `EmailMQConsumer.java` 和 `EmailMQProducer.java`
2. 已清空 `MqAutoConfiguration.java` 中的RabbitMQ配置代码
3. 已从依赖中移除 `cas-spring-boot-starter-mq`
4. 保留同步邮件发送实现：`EmailServiceImpl.java`

**当前状态**：邮件发送功能通过 `@Async` 注解实现异步同步发送，使用Spring Mail直接发送。

---

## 四、安全配置问题 ✅ 已修复

### 4.1 敏感信息硬编码

**修复内容**：
已将敏感信息改为环境变量引用：
```yaml
spring:
  mail:
    username: ${MAIL_USERNAME:dmregy@163.com}
    password: ${MAIL_PASSWORD:}
  datasource:
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:root}

jwt:
  secret: ${JWT_SECRET:}

weather:
  api:
    id: ${WEATHER_API_ID:}
    key: ${WEATHER_API_KEY:}

aliyun:
  oss:
    access-key-id: ${ALIYUN_ACCESS_KEY_ID:}
    access-key-secret: ${ALIYUN_ACCESS_KEY_SECRET:}
    bucket-name: ${ALIYUN_OSS_BUCKET:}
```

**新增文件**：
- `application-local.yml` - 本地开发环境配置文件，提供默认值

---

## 五、代码功能问题

### 5.1 UserServiceImpl功能简化问题 ✅ 已修复

**修复内容**：
- `UserInfoAndServicesViaMPRespVO` 新增 `bookings` 字段
- `UserServiceImpl.getUserInfoAndBookings()` 现在返回用户预约服务列表

---

## 六、项目结构问题

### 6.1 旧src目录 ✅ 已清理

**修复内容**：
- 旧src目录已移动到 `old-src` 目录
- 避免与新架构代码冲突

---

## 七、其他优化

### 7.1 健康检查端点 ✅ 已添加

**修复内容**：
- 添加 `spring-boot-starter-actuator` 依赖
- 配置健康检查端点 `/actuator/health`

---

## 八、待处理问题汇总

| 序号 | 问题 | 优先级 | 状态 |
|------|------|--------|------|
| 1 | 模块依赖优化（appointment依赖system） | 中 | ⚠️ 待处理 |
| 2 | AiChatHistoryMapper在旧src中 | 低 | ⚠️ 待处理 |

---

## 九、修复清单

已修复问题：
- [x] 数据库配置不一致（cas_db → campus_appointment）
- [x] Mapper XML文件位置问题
- [x] Item实体重复问题
- [x] RabbitMQ邮件队列功能清理
- [x] 敏感信息硬编码问题
- [x] UserServiceImpl功能简化问题
- [x] 旧src目录清理
- [x] 健康检查端点添加

待修复问题：
- [ ] 模块依赖优化（需要较大改动）
