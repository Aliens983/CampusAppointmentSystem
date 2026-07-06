# CampusAppointmentSystem — TODO 清单

> 最后更新: 2026-07-06

---

## ✅ 已完成 (2026-07-06 综合优化)

### 角色管理
- [x] **修复角色分配反转 Bug** — `UserMapper.updateRoleToCommonUser` SQL `role=1→0`，`updateRoleToAdmin` SQL `role=0→1`。此前管理员切换为"普通用户"实际仍为管理员。

### 分页查询
- [x] **全局分页** — 9 个列表接口从全量返回改为 `PageResult<T>` 分页响应，支持 `page`/`pageSize` 参数。
  - `GET /admin/service`、`GET /app/service`、`GET /service` — 服务列表
  - `GET /admin/service-status`、`GET /service-status`、`GET /service-status/user` — 预约记录
  - `GET /consultation`、`GET /equipment` — 咨询/设备列表（内存分页）
  - `GET /user/get_all_bookings` — 用户预约记录

### Bean Validation
- [x] **DTO 校验补全** — 为 `AdminCreateUserRequest.role`、`UserRegisterRequest.role` 添加 `@Min(0) @Max(2)`，`ChatReqVO.message` 添加 `@Size(max=4000)`。其余 DTO 已有校验注解。

### 错误码规范
- [x] **统一错误码段** — 消除 HTTP 状态码(200/400/401/403/404/409/429/500)和魔数(3838438/404404404)，统一为业务码段：
  - `1xxxx` — 通用/系统 (`CommonErrorCode`)
  - `2xxxx` — 用户/认证/角色 (`UserErrorCode`，合并 `LoginErrorCode` + `EmailErrorCode` + `RoleErrorCode`)
  - `3xxxx` — 服务 (`ServiceErrorCode`)
  - `4xxxx` — 预约/审核 (`BookErrorCode`，合并 `ServiceStatusErrorCode`)
- [x] 旧接口 (`LoginErrorCode`、`EmailErrorCode`、`RoleErrorCode`、`ServiceStatusErrorCode`) 标记 `@Deprecated`，保留兼容。

### API 版本化
- [x] **添加 `/api/v1` 前缀** — `application.yml` 配置 `server.servlet.context-path=/api/v1`，前端 Vite 代理适配。Controller 代码零改动。

### Redis 业务缓存
- [x] **Spring Cache + Redis** — 新建 `RedisCacheAutoConfiguration`，启用 `@EnableCaching`。
  - `ServiceServiceImpl.getAvailableServices()` → `@Cacheable("services")`
  - `ServiceServiceImpl.getServiceById()` → `@Cacheable("services")`
  - `ServiceServiceImpl.addService()` → `@CacheEvict(value="services", allEntries=true)`
  - `WeatherApiImpl.getWeather()` → `@Cacheable("weather")`
- [x] 默认 TTL 30 分钟，JSON 序列化，禁用 null 值缓存。

### 单元测试
- [x] **从 20 → 64 个测试**（新增 44 个），覆盖 4 个模块 9 个测试类。
  - `cas-module-system`: AuthServiceTest(15) + RoleServiceImplTest(6) + EmailVerificationServiceImplTest(3) + UserServiceImplTest(2) = **26 tests**
  - `cas-module-infra`: EmailServiceImplTest(2) + QRCodeServiceImplTest(3) = **5 tests**
  - `cas-module-appointment`: 原有 20 + ServiceServiceImplTest(7) = **27 tests**
  - `cas-thirdparty`: WeatherApiImplTest(4) + SmsServiceImplTest(2) = **6 tests**

---

## 🔴 高优先级 (安全 & 数据完整性)

### 安全加固
- [ ] **RoleAspect 重构** — 当前手动写 JSON 到 `HttpServletResponse`，绕过 `GlobalExceptionHandler`。应改为抛出异常由全局处理器统一格式化响应。
- [ ] **JWT Refresh Token 机制** — 当前仅有无状态 JWT（24h 过期），无 refresh token。Token 过期后用户需重新登录，体验差。
- [ ] **接口幂等性** — 防止重复提交预约。建议：前端按钮 loading 防抖 + 后端基于 `userId:serviceId` 的 Redis 分布式锁（5s TTL）或数据库唯一约束。
- [ ] **敏感操作日志** — 登录、角色变更、审核操作缺少审计日志（目前仅 `@Slf4j` 打日志到控制台）。建议存入数据库或 ELK。

### 数据完整性
- [ ] **数据库唯一约束** — `item` 表缺少 `UNIQUE(user_id, service_id)` 防止同一用户重复预约同一服务。
- [ ] **事务边界扩展** — 当前仅 `BookServiceImpl.bookService()` 有 `@Transactional`。审核操作（更新状态 + 发邮件）应纳入事务或使用 Saga 模式。

---

## 🟡 中优先级 (功能完善)

### 业务功能
- [ ] **假数据替换** — `ConsultationServiceImpl` 和 `EquipmentServiceImpl` 返回硬编码假数据（假评分、假库存、假咨询师姓名）。需对接真实数据源或数据库表。
- [ ] **预约时间段** — 当前预约仅记录日期，无具体时间段。建议添加 `start_time`/`end_time` 字段，防止时间冲突。
- [ ] **预约取消时限** — 当前用户可随时取消预约。建议：审核通过后 N 小时内不可取消，或需管理员审批取消。
- [ ] **消息通知中心** — 当前邮件通知散落在 `ServiceStatusServiceImpl` 中。建议抽取统一的 `NotificationService`，支持邮件 + 站内信 + 短信多渠道。

### 前端
- [ ] **前端测试** — 18 个页面零测试。建议：Vitest + Vue Test Utils 覆盖核心组件，Playwright 做 E2E 冒烟测试。
- [ ] **Token 自动刷新** — Axios 拦截器响应 401 → 尝试 refresh token → 失败跳转登录页。
- [ ] **国际化 i18n** — `vue-i18n` 依赖已安装但未接入。页面中文硬编码，需抽离语言包。
- [ ] **前端错误统一处理** — 各页面 `try-catch` 散落，建议 Axios 拦截器统一弹出错误提示。

### 测试补充
- [ ] **Controller 层集成测试** — 使用 `@WebMvcTest` + MockMvc 测试 REST 接口的请求/响应完整流程。
- [ ] **Repository 层集成测试** — 使用 `@MybatisPlusTest` + H2 内存数据库测试 Mapper SQL。
- [ ] **边界值测试** — 分页参数 `page=0`/`page=-1`/`pageSize=0`/`pageSize=10000` 的防御性处理。

---

## 🟢 低优先级 (基础设施 & 架构演进)

### DevOps
- [ ] **Docker Compose 一键部署** — `docker-compose.yml` 包含 MySQL + Redis + 后端 + 前端 Nginx，新人一键启动。
- [ ] **Flyway 数据库迁移** — 替代手动执行 `sql/` 目录下的 SQL 脚本，版本化管理 DDL 变更。
- [ ] **CI/CD Pipeline** — GitHub Actions: Push → 编译 + 测试 → Docker 构建 → 推送镜像。
- [ ] **环境配置分离** — `application-dev.yml` / `application-prod.yml`，通过 `spring.profiles.active` 切换。

### 架构优化
- [ ] **消息队列落地** — `cas-spring-boot-starter-mq` 是空壳。建议 RabbitMQ 异步处理：邮件发送、审核通知、数据统计。
- [ ] **贫血模型改造** — `User`、`AppointmentRecord`、`AiChatHistory` 无行为方法。将业务逻辑（如 `AppointmentRecord.cancel()`、`User.changePassword()`）内聚到领域实体。
- [ ] **DTO 命名统一** — 当前混用 `*ReqVO`、`*Request`、`*DTO`、`*Response`、`*RespVO`。建议统一为 `*Request` / `*Response`。
- [ ] **API 文档完善** — Knife4j 已有基础配置，但部分接口缺少 `@Schema` 描述和示例值。
- [ ] **DeepSeekConfig 清理** — `DeepSeekConfig.java` 从未被注入或使用，属于死代码。

### 监控 & 可观测性
- [ ] **健康检查增强** — Actuator 已暴露 `/actuator/health`，建议添加 Redis、MySQL 连通性检查。
- [ ] **接口性能监控** — 添加 `Micrometer` + `Prometheus` 指标采集，监控慢接口。
- [ ] **全局请求日志** — 添加 Filter/Interceptor 记录每个请求的耗时、参数、响应码。

---

## 📊 完成度总览

| 模块 | 测试覆盖 | 分页 | 校验 | 缓存 | 错误码 | API版本 |
|------|:--:|:--:|:--:|:--:|:--:|:--:|
| cas-common | — | — | — | — | ✅ | — |
| cas-framework | — | — | — | ✅ | — | — |
| cas-module-system | 26 tests | ✅ | ✅ | — | ✅ | ✅ |
| cas-module-appointment | 27 tests | ✅ | ✅ | ✅ | ✅ | ✅ |
| cas-module-infra | 5 tests | — | ✅ | — | ✅ | ✅ |
| cas-thirdparty | 6 tests | — | ✅ | ✅ | ✅ | ✅ |
| cas-server | — | — | — | — | — | ✅ |
| **前端** | 0 tests | ✅ | — | — | — | ✅ |

---

## 🗓️ 建议实施路线

```
第1周:  RoleAspect 重构 + 幂等性 + 事务边界
第2周:  假数据替换 + Controller 集成测试
第3-4周: Docker Compose + Flyway + CI/CD
第5周+: 消息队列 + 贫血模型改造 + 前端测试
```
