### JWT工作原理
JWT（JSON Web Token）是一种开放标准（RFC 7519），用于在网络应用间安全地传递声明（claims）。它由三部分组成，用点号（.）分隔：

- Header（头部） ：包含算法和token类型
- Payload（载荷） ：包含声明（用户信息等）
- Signature（签名） ：用于验证消息未被篡改
### 整个HTTP带Token请求到回应的流程
1. 用户登录阶段 ：

    - 用户提交用户名和密码
    - 服务器验证凭据
    - 验证成功后，服务器使用密钥创建JWT
    - 服务器将JWT返回给客户端
2. 客户端存储Token ：

    - 前端将JWT存储在localStorage、sessionStorage或Cookie中
3. 后续请求阶段 ：

    - 客户端发起请求时，在请求头中加入JWT
    - 格式为： Authorization: Bearer <JWT_TOKEN>
    - 服务器接收请求，从请求头中提取JWT
    - 服务器验证JWT的签名和有效期
    - 验证通过后，解析出用户信息，处理业务逻辑
    - 返回响应给客户端

关于MyBatis-x插件的使用:

![image-20260316102504204](C:\Users\25516\AppData\Roaming\Typora\typora-user-images\image-20260316102504204.png)

这里的basepackage填写基础包名即可:如com.laoliu.system

relative package (相对包名) 可以填写entity等

然后default all

即可

我应该如何区分普通用户和管理员用户呢?
You can distinguish them by parsing token.
如何创建管理员用户呢?
Maybe you can set three role totals that include common user, admin and super admin.
实际生产中应该是怎么样的
如果我在创建用户时候, 在用户的实体类设置了一个属性,但是在数据库中我没有把这个属性添加到数据库表当中,如果我在后端代码中设置好了这个实体类的属性那么他的属性是存储在哪里的?
