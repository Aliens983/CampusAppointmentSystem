# Kubernetes (K8s) 介绍

## 什么是 Kubernetes

Kubernetes（简称 K8s）是一个**容器编排平台**，用于自动管理多台机器上的容器化应用。

简单类比：
- **Docker Compose** 管 **一台机器** 上的几个容器
- **Kubernetes** 管 **一群机器** 上的成百上千个容器

---

## 核心概念

### 集群 (Cluster)
K8s 管理一组服务器（节点），组成一个集群。

### 节点 (Node)
- **Master 节点**：控制面，管理整个集群
- **Worker 节点**：运行实际应用容器

### Pod
K8s 最小的部署单元，一个 Pod 包含一个或多个容器。

### Deployment
声明式地管理 Pod 的副本数、滚动更新策略。

### Service
为 Pod 提供稳定的网络入口和负载均衡。

---

## Docker Compose vs K8s

| 特性 | Docker Compose | Kubernetes |
|---|---|---|
| 管理范围 | 单台机器 | 集群（多台机器） |
| 自动扩容 | ❌ 需手动改配置 | ✅ 支持 HPA 自动伸缩 |
| 滚动更新 | ❌ | ✅ 零停机更新 |
| 自愈能力 | 部分（restart: always） | ✅ 自动重启、重新调度 |
| 服务发现 | ❌ | ✅ 内置 DNS |
| 负载均衡 | ❌ 需额外配置 | ✅ 内置 Service |
| 存储卷管理 | 基本 | ✅ 支持多种存储后端 |
| 配置中心 | ❌ | ✅ ConfigMap / Secret |
| 学习曲线 | 低 | 高 |
| 运维复杂度 | 低 | 高 |

---

## 什么时候该用 K8s

### 建议使用
- 有 **3 台以上服务器** 组成集群
- 微服务数量超过 **5-10 个**
- 需要 **自动扩容** 应对流量波动
- 需要 **滚动更新**（不停机发布）
- 团队有专门的运维/基础设施人员

### 不建议使用（你的项目属于此类）
- 只有 **1-2 台服务器**
- 服务数量 **少于 5 个**
- 团队对 K8s 不熟悉
- **就像本项目**：单服务器 + 3 个服务（App + MySQL + Redis），Docker Compose 完全够用

---

## Minikube：本地体验 K8s

```bash
# 安装 minikube
curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64
sudo install minikube-linux-amd64 /usr/local/bin/minikube

# 启动集群
minikube start

# 查看状态
kubectl get nodes
```

---

## 本项目的部署方式

本项目采用 **Docker Compose** 部署到单台服务器，原因：
1. 只有 3 个服务，架构简单
2. 单台服务器，无集群需求
3. 无需自动扩容
4. 降低运维复杂度

如果未来需要迁移到 K8s，项目已经 Docker 化了，只需要将 docker-compose.yml 转换为 K8s YAML 即可（可使用 `kompose convert` 辅助转换）。

---

## 推荐学习资源

- [官方文档](https://kubernetes.io/docs/home/)
- [Kubernetes 中文社区](https://www.kubernetes.org.cn/)
- [Play with Kubernetes](https://labs.play-with-k8s.com/) — 在线体验
