# 部署待办清单

## 1. 配置 GitHub Secrets

在 GitHub 仓库页面：**Settings → Secrets and variables → Actions**

添加以下四个 Secret：

| Secret | 说明 | 示例 |
|---|---|---|
| `DEPLOY_HOST` | 服务器 IP 或域名 | `123.456.789.0` 或 `your-server.com` |
| `DEPLOY_USER` | SSH 登录用户名 | `root` |
| `DEPLOY_SSH_KEY` | 服务器的 SSH 私钥 | `-----BEGIN OPENSSH PRIVATE KEY-----\n...` |
| `DEPLOY_PORT` | SSH 端口（可选，默认 22） | `22` |

## 2. 准备服务器

```bash
# 连接服务器
ssh root@你的服务器IP

# 安装 Docker（如果没有）
curl -fsSL https://get.docker.com | sh

# 创建部署目录
mkdir -p /opt/cas

# 上传必要文件到 /opt/cas/
# 需要的文件：
#   - docker-compose.yml
#   - .env
```

## 3. 配置 .env 文件

在服务器 `/opt/cas/.env` 中填入真实值：

```bash
MYSQL_ROOT_PASSWORD=你的数据库密码
QWEN_API_KEY=你的通义千问API密钥
SERVER_ADDRESS=0.0.0.0
```

> `.env` **不要提交到 Git**，里面包含密码和 API Key。

## 4. 测试部署

全部配置好后，可以：

- **方式一**：推送到 `No_Multiple_Maven_Modules` 分支自动触发 CI/CD
- **方式二**：在 GitHub Actions 页面手动点击 "Run workflow"
- **方式三**：直接在服务器上测试：

```bash
cd /opt/cas
docker compose up -d
docker compose logs -f app
```

## 5. 验证部署

```bash
# 检查所有服务状态
docker compose ps

# 查看应用日志
docker compose logs app

# 测试健康检查接口
curl http://你的服务器IP:8080/actuator/health
```

## 6. （可选）切换到 main 分支

当需要正式上线时，把 `No_Multiple_Maven_Modules` 分支合并到 `master`，并修改 `deploy.yml` 中的触发分支：

```yaml
on:
  push:
    branches: [master]
```

然后更新 `.github/workflows/deploy.yml` 中的条件：

```yaml
if: github.ref == 'refs/heads/master'
```
