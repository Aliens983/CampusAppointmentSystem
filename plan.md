# 模块重构计划 - cas-common 模块清理

## 当前问题分析

当前 `cas-common` 模块包含了大量不应该放在公共模块的类，导致模块职责不清、依赖关系混乱。

### 当前 cas-common 模块结构
```
cas-common/
├── api/                           # API 接口
│   ├── GetUserIdViaTokenApi.java   # 用户ID获取（通用接口，保留）
│   ├── UserInfoApi.java            # 用户信息服务（通用接口，保留）
│   ├── WeatherApi.java             # ❌ 应移至 cas-thirdparty
│   └── WeatherResponse.java        # ❌ 应移至 cas-thirdparty
├── domain/entity/                  # 实体类
│   ├── User.java                   # ❌ 应移至 cas-module-system
│   ├── AppointmentRecord.java      # ❌ 应移至 cas-module-appointment
│   ├── Services.java               # ❌ 应移至 cas-module-appointment
│   ├── Log.java                    # ⚠️ 待确认
│   ├── FileEntity.java             # ⚠️ 待确认
│   └── AiChatHistory.java          # ❌ 应移至 cas-thirdparty
├── vo/                             # VO 对象
│   ├── request/ChatReqVO.java     # ❌ 应移至 cas-thirdparty
│   └── response/ChatRespVO.java    # ❌ 应移至 cas-thirdparty
├── service/                        # 服务接口
│   ├── CallModelService.java       # ❌ 应移至 cas-thirdparty
│   ├── EmailService.java           # ❌ 应移至 cas-module-infra
│   ├── OSSService.java             # ❌ 应移至 cas-thirdparty
│   └── QRCodeService.java          # ❌ 应移至 cas-module-infra
└── ...其他通用类（保留）
```

## 迁移计划

### 阶段一：Weather 相关类迁移到 cas-thirdparty

**目标**：将 WeatherApi、WeatherResponse 从 cas-common 移至 cas-thirdparty

#### 步骤 1.1：创建目标包结构
- 在 `cas-thirdparty/api/` 下创建 `request/` 和 `response/` 包

#### 步骤 1.2：迁移 WeatherResponse.java
- 从 `cas-common/api/WeatherResponse.java`
- 移至 `cas-thirdparty/api/response/WeatherResponse.java`

#### 步骤 1.3：迁移 WeatherApi.java
- 从 `cas-common/api/WeatherApi.java`
- 移至 `cas-thirdparty/api/WeatherApi.java`
- 更新 package 声明
- 更新 import 语句（WeatherResponse 改为相对引用）

#### 步骤 1.4：更新 WeatherApiImpl.java
- 文件位置：`cas-thirdparty/api/impl/WeatherApiImpl.java`
- 更新 import：`WeatherResponse` → `com.laoliu.cas.thirdparty.api.response.WeatherResponse`

#### 步骤 1.5：更新 WeatherController.java
- 文件位置：`cas-thirdparty/interfaces/WeatherController.java`
- 更新 import：`com.laoliu.cas.common.api.WeatherResponse` → `com.laoliu.cas.thirdparty.api.response.WeatherResponse`

#### 步骤 1.6：删除原文件
- 删除 `cas-common/api/WeatherResponse.java`
- 删除 `cas-common/api/WeatherApi.java`

#### 步骤 1.7：更新 cas-common 的依赖配置
- 更新 pom.xml（如果存在）移除 WeatherResponse 依赖

---

### 阶段二：Chat 相关的 VO 和服务迁移到 cas-thirdparty

**目标**：将 ChatReqVO、ChatRespVO、CallModelService 从 cas-common 移至 cas-thirdparty

#### 步骤 2.1：创建目标包结构
- 在 `cas-thirdparty/api/` 下创建 `vo/request/` 和 `vo/response/` 包

#### 步骤 2.2：迁移 ChatReqVO.java
- 从 `cas-common/vo/request/ChatReqVO.java`
- 移至 `cas-thirdparty/api/vo/request/ChatReqVO.java`
- 更新 package 声明

#### 步骤 2.3：迁移 ChatRespVO.java
- 从 `cas-common/vo/response/ChatRespVO.java`
- 移至 `cas-thirdparty/api/vo/response/ChatRespVO.java`
- 更新 package 声明

#### 步骤 2.4：迁移 CallModelService.java 接口
- 从 `cas-common/service/CallModelService.java`
- 移至 `cas-thirdparty/service/CallModelService.java`
- 更新 package 声明
- 更新 import 语句（ChatReqVO、ChatRespVO 改为相对引用）

#### 步骤 2.5：更新 CallModelServiceImpl.java
- 文件位置：`cas-thirdparty/service/impl/CallModelServiceImpl.java`
- 更新 import：
  - `ChatReqVO` → `com.laoliu.cas.thirdparty.api.vo.request.ChatReqVO`
  - `ChatRespVO` → `com.laoliu.cas.thirdparty.api.vo.response.ChatRespVO`
  - `CallModelService` → `com.laoliu.cas.thirdparty.service.CallModelService`

#### 步骤 2.6：更新 CallTheModelController.java
- 文件位置：`cas-thirdparty/interfaces/CallTheModelController.java`
- 更新 import：
  - `ChatReqVO` → `com.laoliu.cas.thirdparty.api.vo.request.ChatReqVO`
  - `ChatRespVO` → `com.laoliu.cas.thirdparty.api.vo.response.ChatRespVO`
  - `CallModelService` → `com.laoliu.cas.thirdparty.service.CallModelService`

#### 步骤 2.7：删除原文件
- 删除 `cas-common/vo/request/ChatReqVO.java`
- 删除 `cas-common/vo/response/ChatRespVO.java`
- 删除 `cas-common/service/CallModelService.java`

---

### 阶段三：实体类迁移到对应业务模块

**目标**：将业务相关的实体类从 cas-common 移至各自的业务模块

#### 步骤 3.1：迁移 User.java
- 从 `cas-common/domain/entity/User.java`
- 移至 `cas-module-system/domain/entity/User.java`
- 更新 package 声明
- 更新所有引用该类的文件（见下）

需要更新的文件：
- `cas-module-system/interfaces/assembler/UserAssembler.java`
- `cas-module-system/interfaces/controller/admin/UserController.java`
- `cas-module-system/application/service/UserService.java`
- `cas-module-system/application/service/impl/UserServiceImpl.java`
- `cas-module-system/infrastructure/persistence/mapper/UserMapper.java`
- `cas-module-system/api/impl/UserInfoApiImpl.java`
- `cas-common/api/UserInfoApi.java`（接口返回类型）

#### 步骤 3.2：迁移 AppointmentRecord.java
- 从 `cas-common/domain/entity/AppointmentRecord.java`
- 移至 `cas-module-appointment/domain/entity/AppointmentRecord.java`
- 更新 package 声明
- 更新所有引用该类的文件

需要更新的文件：
- `cas-module-appointment/application/service/BookService.java`
- `cas-module-appointment/application/service/impl/BookServiceImpl.java`

#### 步骤 3.3：迁移 Services.java
- 从 `cas-common/domain/entity/Services.java`
- 移至 `cas-module-appointment/domain/entity/Services.java`
- 更新 package 声明
- 更新所有引用该类的文件

需要更新的文件：
- `cas-module-appointment/application/service/ServicesService.java`
- `cas-module-appointment/application/service/impl/ServicesServiceImpl.java`
- `cas-module-appointment/infrastructure/persistence/mapper/ServicesMapper.java`
- `cas-module-appointment/interfaces/controller/ServiceController.java`

#### 步骤 3.4：迁移 AiChatHistory.java
- 从 `cas-common/domain/entity/AiChatHistory.java`
- 移至 `cas-thirdparty/domain/entity/AiChatHistory.java`
- 更新 package 声明
- 更新所有引用该类的文件

需要更新的文件：
- `cas-thirdparty/infrastructure/persistence/mapper/AiChatHistoryMapper.java`
- `cas-thirdparty/service/impl/CallModelServiceImpl.java`

#### 步骤 3.5：待确认的实体类
- **Log.java** - 需要确认使用范围
- **FileEntity.java** - 需要确认使用范围

---

### 阶段四：服务接口迁移

**目标**：将基础设施服务接口从 cas-common 移至对应模块

#### 步骤 4.1：迁移 EmailService.java
- 从 `cas-common/service/EmailService.java`
- 移至 `cas-module-infra/service/EmailService.java`
- 更新 package 声明
- 更新所有引用该类的文件

需要更新的文件：
- `cas-module-infra/application/service/impl/EmailServiceImpl.java`
- `cas-module-system/interfaces/controller/EmailController.java`
- `cas-module-appointment/application/service/impl/ServiceStatusServiceImpl.java`

#### 步骤 4.2：迁移 QRCodeService.java
- 从 `cas-common/service/QRCodeService.java`
- 移至 `cas-module-infra/service/QRCodeService.java`
- 更新 package 声明
- 更新所有引用该类的文件

需要更新的文件：
- `cas-module-infra/interfaces/controller/QRCodeController.java`
- `cas-module-infra/application/service/impl/QRCodeServiceImpl.java`

#### 步骤 4.3：迁移 OSSService.java
- 从 `cas-common/service/OSSService.java`
- 移至 `cas-thirdparty/service/OSSService.java`
- 更新 package 声明
- 更新所有引用该类的文件

需要更新的文件：
- `cas-thirdparty/service/impl/OSSServiceImpl.java`
- `cas-module-infra/interfaces/controller/OSSController.java`

---

### 阶段五：清理和验证

#### 步骤 5.1：删除空的包目录
- 删除 `cas-common/api/` 下迁移后遗留的空包
- 删除 `cas-common/domain/entity/` 下迁移后遗留的空包
- 删除 `cas-common/vo/` 下迁移后遗留的空包
- 删除 `cas-common/service/` 下迁移后遗留的空包

#### 步骤 5.2：运行编译验证
- 执行 `mvn clean compile` 验证所有迁移是否成功
- 检查是否有遗漏的 import 或引用

#### 步骤 5.3：运行测试验证
- 执行 `mvn test` 确保所有测试通过

---

## 保留在 cas-common 模块的类

以下类应该保留在 `cas-common` 模块，因为它们是真正的通用基础设施：

```
cas-common/
├── annotation/
│   └── RequireRole.java           # ✅ 保留 - 通用权限注解
├── api/
│   ├── GetUserIdViaTokenApi.java   # ✅ 保留 - 通用用户ID获取接口
│   └── UserInfoApi.java            # ✅ 保留 - 通用用户信息服务接口
├── enums/                          # ✅ 全部保留 - 通用枚举
│   ├── ManageStatus.java
│   ├── ServiceStatus.java
│   └── UserRoleEnum.java
├── exception/                      # ✅ 全部保留 - 通用异常和错误码
│   ├── BusinessException.java
│   ├── ErrorCode.java
│   ├── ForbiddenException.java
│   ├── ResourceNotFoundException.java
│   ├── UnauthorizedException.java
│   └── code/
│       ├── BookErrorCode.java
│       ├── CommonErrorCode.java
│       ├── EmailErrorCode.java
│       ├── LoginErrorCode.java
│       ├── RoleErrorCode.java
│       ├── ServiceErrorCode.java
│       ├── ServiceStatusErrorCode.java
│       └── UserErrorCode.java
├── result/
│   └── CommonResult.java           # ✅ 保留 - 通用响应封装
├── security/
│   └── LoginUser.java              # ✅ 保留 - 通用登录用户实体
├── util/                           # ✅ 全部保留 - 通用工具类
│   ├── CodeGenerator.java
│   └── PasswordUtils.java
└── ...其他通用类
```

---

## 执行顺序建议

1. **阶段一（Weather）** - 最简单，只涉及 cas-thirdparty 一个模块
2. **阶段二（Chat/CallModel）** - 涉及 cas-thirdparty 一个模块
3. **阶段三（实体类）** - 涉及多个模块，需要小心处理 import
4. **阶段四（服务接口）** - 涉及多个模块的交叉引用
5. **阶段五（清理验证）** - 最终验证和清理

**注意**：每个阶段完成后都应进行编译验证，确保没有遗漏。
