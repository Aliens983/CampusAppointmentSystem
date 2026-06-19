# CI/CD 实现方案

## 概述

本项目使用 **GitHub Actions** 实现 CI/CD 流水线，包含三个步骤：**测试 → 构建镜像并推送 → 部署到服务器**。

流水线定义文件：`.github/workflows/deploy.yml`

---

## 工作原理

```mermaid
graph LR
    A[Push 到 GitHub] --> B[test 作业]
    B -->|通过| C[build-and-push 作业]
    C -->|通过| D[deploy 作业]
    D --> E[服务器拉取新镜像并重启]
```

### 1. test — 运行测试

```yaml
test:
  runs-on: ubuntu-latest
  steps:
    - uses: actions/checkout@v4
    - name: Set up JDK 17
      uses: actions/setup-java@v4
    - name: Run tests
      run: mvn -B test
```

- 使用 `actions/checkout@v4` 拉取代码
- 用 `setup-java` 配置 JDK 17 + Maven 缓存
- 执行 `mvn -B test` 运行全部 212 个单元测试
- **如果测试失败，后续步骤不执行**

### 2. build-and-push — 构建并推送 Docker 镜像

```yaml
build-and-push:
  needs: test
  permissions:
    packages: write
  steps:
    - name: Lowercase repository name
      run: echo "IMAGE_NAME=${GITHUB_REPOSITORY,,}" >> $GITHUB_ENV
    - name: Log in to GHCR
      uses: docker/login-action@v3
    - name: Build and push
      uses: docker/build-push-action@v5
```

关键细节：
- `needs: test` — 等待测试通过后才执行
- 使用 `${GITHUB_REPOSITORY,,}` 将仓库名转为**小写**（Docker tag 必须全小写）
- 登录到 **GitHub Container Registry (ghcr.io)**
- 构建两个 tag：`:latest` 和 `:${{ github.sha }}`
- 使用 GitHub Actions cache（`type=gha`）加速后续构建

### 3. deploy — SSH 部署到服务器

```yaml
deploy:
  needs: build-and-push
  if: github.ref == 'refs/heads/No_Multiple_Maven_Modules'
  steps:
    - uses: appleboy/ssh-action@v1
```

- 使用 [appleboy/ssh-action](https://github.com/appleboy/ssh-action) 通过 SSH 连接服务器
- 在服务器上执行：
  1. `cd /opt/cas`
  2. `docker compose pull app` — 拉取最新镜像
  3. `docker compose up -d --remove-orphans` — 重启服务
  4. `docker image prune -f` — 清理旧镜像

---

## 触发条件

- **自动触发**：推送到 `No_Multiple_Maven_Modules` 分支
- **手动触发**：在 GitHub 仓库 Actions 页面点击 "Run workflow"

---

## 服务器部署清单

在目标服务器上准备：

```bash
# 创建部署目录
mkdir -p /opt/cas

# 上传必要文件
# docker-compose.yml 和 .env 到 /opt/cas/

# 确保 Docker 和 docker compose 已安装
docker --version
docker compose version
```

### GitHub Secrets 配置

在仓库 Settings → Secrets and variables → Actions 中添加：

| Secret | 说明 |
|---|---|
| `DEPLOY_HOST` | 服务器 IP 或域名 |
| `DEPLOY_USER` | SSH 用户名 |
| `DEPLOY_SSH_KEY` | SSH 私钥 |
| `DEPLOY_PORT` | SSH 端口（可选，默认 22） |

---

## 项目文件结构

```
CampusAppointmentSystem/
├── Dockerfile                  # 多阶段构建
├── .dockerignore               # 构建上下文排除
├── docker-compose.yml          # 服务编排
├── .env.example                # 环境变量模板
├── .github/workflows/
│   └── deploy.yml              # CI/CD 流水线
└── src/main/resources/
    └── application-docker.yml  # Docker 环境配置
```

---

## 技术要点

1. **多阶段构建**：第一阶段用 Maven 镜像编译，第二阶段用 JRE 镜像运行，最终镜像仅 269MB
2. **网络问题**：Maven 基础镜像中 DNS 可能无法解析 Maven Central，使用 `--network=host` 解决
3. **大小写敏感**：Docker tag 必须全小写，通过 `${GITHUB_REPOSITORY,,}` 转换
4. **Git 子模块**：项目使用子模块结构，CI/CD 配置文件放在子模块仓库内
