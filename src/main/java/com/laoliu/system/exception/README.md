# 全局异常处理使用指南

## 概述

全局异常处理器已经配置完成，可以统一处理应用中的所有异常，返回统一格式的错误响应。

## 文件结构

```
exception/
├── GlobalExceptionHandler.java          # 全局异常处理器
├── BusinessException.java               # 业务异常基类
├── UnauthorizedException.java          # 未授权异常
├── ForbiddenException.java             # 禁止访问异常
└── ResourceNotFoundException.java      # 资源不存在异常

common/exception/enums/
├── CommonErrorCode.java                 # 通用错误码
├── BusinessErrorCode.java               # 业务错误码
└── BookErrorCode.java                   # 预约相关错误码
```

## 错误码分类

### 通用错误码 (CommonErrorCode)
- 200: 操作成功
- 400: 请求参数错误
- 401: 未授权
- 403: 禁止访问
- 404: 资源不存在
- 500: 服务器内部错误

### 业务错误码 (BusinessErrorCode)
- 1000-1099: 用户相关
- 1100-1199: 认证授权相关
- 1200-1299: 验证码相关
- 1300-1399: 预约相关
- 1400-1499: 文件相关
- 1500-1599: 系统相关

### 模块错误码 (如 BookErrorCode)
- 预约相关的特定错误码

## 使用示例

### 1. 抛出业务异常

```java
// 使用自定义异常
throw new BusinessException("业务逻辑错误");

// 使用错误码
throw new BusinessException(BusinessErrorCode.USER_NOT_FOUND);

// 带参数的错误消息
throw new BusinessException(BusinessErrorCode.USER_LOGIN_FAILED, "用户名或密码错误");
```

### 2. 抛出资源不存在异常

```java
// 当查询不到资源时
if (user == null) {
    throw new ResourceNotFoundException("用户不存在");
}
```

### 3. 抛出未授权异常

```java
// 当用户未登录或Token无效时
if (!isAuthenticated()) {
    throw new UnauthorizedException("请先登录");
}
```

### 4. 抛出禁止访问异常

```java
// 当用户权限不足时
if (!hasPermission()) {
    throw new ForbiddenException("您没有权限执行此操作");
}
```

### 5. 参数校验异常（自动处理）

```java
// 使用 @Valid 注解，异常会被自动捕获
@PostMapping
public CommonResult<Void> create(@Valid @RequestBody UserDTO dto) {
    // ...
}
```

### 6. 在 Service 层使用

```java
@Service
public class UserService {

    @Transactional
    public User updateUser(Long userId, UserUpdateDTO dto) {
        // 查询用户
        User user = userMapper.selectById(userId);
        if (user == null) {
            // 抛出异常，会被全局异常处理器捕获
            throw new ResourceNotFoundException("用户不存在");
        }
        
        // 更新用户信息
        user.setName(dto.getName());
        userMapper.updateById(user);
        
        return user;
    }
}
```

### 7. 在 Controller 层使用

```java
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/{id}")
    public CommonResult<User> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new ResourceNotFoundException("用户不存在");
        }
        return CommonResult.success(user);
    }

    @PutMapping
    public CommonResult<Void> updateUser(@Valid @RequestBody UserUpdateDTO dto) {
        // 参数校验失败会自动抛出 MethodArgumentNotValidException
        // 被全局异常处理器捕获并返回 400 错误
        userService.updateUser(dto);
        return CommonResult.success("更新成功");
    }
}
```

## 异常处理流程

1. **Controller 层抛出异常**
2. **GlobalExceptionHandler 捕获异常**
3. **根据异常类型匹配对应的处理方法**
4. **返回统一格式的 CommonResult 响应**
5. **记录错误日志**

## 响应格式

### 成功响应
```json
{
  "code": 200,
  "message": "操作成功",
  "data": { ... }
}
```

### 错误响应示例

**参数校验失败 (400)**
```json
{
  "code": 400,
  "message": "用户名不能为空;邮箱格式不正确;",
  "data": null
}
```

**资源不存在 (404)**
```json
{
  "code": 404,
  "message": "用户不存在",
  "data": null
}
```

**未授权 (401)**
```json
{
  "code": 401,
  "message": "请先登录",
  "data": null
}
```

**业务异常 (500)**
```json
{
  "code": 500,
  "message": "服务器内部错误: 用户不存在",
  "data": null
}
```

## 最佳实践

1. **在 Service 层抛出异常**，Controller 层不需要捕获
2. **使用具体的异常类型**，如 ResourceNotFoundException 而不是 RuntimeException
3. **使用错误码枚举**，便于维护和国际化
4. **异常消息要清晰明确**，便于调试和用户理解
5. **记录关键业务日志**，方便问题排查
6. **避免在 Controller 中使用 try-catch**，让全局异常处理器统一处理

## 注意事项

1. 全局异常处理器会捕获所有未处理的异常
2. 参数校验异常（@Valid）会自动被处理
3. 自定义异常需要继承 BusinessException 或其他自定义异常类
4. 错误码建议按模块分类，避免冲突
5. 生产环境建议使用更详细的日志记录