# ============================================================
# 后端 Dockerfile — 多阶段构建
# 阶段1: Maven 构建 (含所有模块依赖)
# 阶段2: JRE 运行 (精简镜像)
# ============================================================

# --- 构建阶段 ---
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

# 1. 先复制 POM 文件（利用 Docker 缓存层）
COPY pom.xml ./
COPY cas-dependencies/pom.xml ./cas-dependencies/
COPY cas-framework/pom.xml ./cas-framework/
COPY cas-framework/cas-common/pom.xml ./cas-framework/cas-common/
COPY cas-framework/cas-spring-boot-starter-web/pom.xml ./cas-framework/cas-spring-boot-starter-web/
COPY cas-framework/cas-spring-boot-starter-security/pom.xml ./cas-framework/cas-spring-boot-starter-security/
COPY cas-framework/cas-spring-boot-starter-mybatis/pom.xml ./cas-framework/cas-spring-boot-starter-mybatis/
COPY cas-framework/cas-spring-boot-starter-redis/pom.xml ./cas-framework/cas-spring-boot-starter-redis/
COPY cas-framework/cas-spring-boot-starter-mq/pom.xml ./cas-framework/cas-spring-boot-starter-mq/
COPY cas-framework/cas-spring-boot-starter-test/pom.xml ./cas-framework/cas-spring-boot-starter-test/
COPY cas-module-infra/pom.xml ./cas-module-infra/
COPY cas-module-system/pom.xml ./cas-module-system/
COPY cas-module-appointment/pom.xml ./cas-module-appointment/
COPY cas-thirdparty/pom.xml ./cas-thirdparty/
COPY cas-server/pom.xml ./cas-server/

# 2. 下载依赖（此层可被缓存直到 POM 变化）
RUN mvn dependency:go-offline -B -DskipTests

# 3. 复制源码
COPY cas-framework/ ./cas-framework/
COPY cas-module-infra/ ./cas-module-infra/
COPY cas-module-system/ ./cas-module-system/
COPY cas-module-appointment/ ./cas-module-appointment/
COPY cas-thirdparty/ ./cas-thirdparty/
COPY cas-server/ ./cas-server/

# 4. 构建（跳过测试）
RUN mvn clean package -DskipTests -B

# --- 运行阶段 ---
FROM eclipse-temurin:17-jre-alpine

# 设置时区
RUN apk add --no-cache tzdata curl && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone && \
    apk del tzdata

WORKDIR /app

# 从构建阶段复制 JAR
COPY --from=builder /build/cas-server/target/cas-server-*.jar app.jar

# 创建上传目录
RUN mkdir -p /app/uploads

EXPOSE 18080

# JVM 参数可通过 JAVA_OPTS 环境变量覆盖
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS:--Xms256m -Xmx512m} -jar app.jar"]
