# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

Maven multi-module project, Java 17, Spring Boot 3.3.5. There is no `mvnw` — use the system `mvn`.

```bash
# Full build (skip tests; there are currently no test sources)
mvn clean package -DskipTests

# Compile only a single module (builds its framework deps first)
mvn -pl cas-module-appointment -am clean compile

# Run the app (entry point is cas-server)
java -jar cas-server/target/cas-server-1.0.0.jar
# App listens on port 18080 (see application.yml), not 8080.
```

Tests: `cas-spring-boot-starter-test` provides `BaseApplicationTest` (`@SpringBootTest`), but no `src/test` sources exist yet. To run a single test once added: `mvn -pl <module> test -Dtest=ClassName#method`.

## Module Architecture

Group `com.laoliu`, base package `com.laoliu.cas`. The reactor is layered bottom-up:

```
cas-dependencies      BOM — centralizes all third-party versions (imported by root pom)
cas-framework (pom)   Technical starters, auto-configured via META-INF/spring/*.imports
  ├ cas-common              Shared kernel: CommonResult, exceptions/error codes, enums,
  │                         LoginUser/JWTUtils, SecurityFrameworkUtils, GetUserIdViaTokenApi,
  │                         @RequireRole, utils. Keep LEAN — see "cas-common discipline" below.
  ├ cas-spring-boot-starter-web        GlobalExceptionHandler, WebAutoConfiguration
  ├ cas-spring-boot-starter-security   JWTFilter + SecurityAutoConfiguration (stateless, JWT)
  ├ cas-spring-boot-starter-mybatis    MyBatis-Plus config
  ├ cas-spring-boot-starter-redis
  ├ cas-spring-boot-starter-mq
  └ cas-spring-boot-starter-test       BaseApplicationTest
cas-module-infra      Infrastructure services (files, QR, email) — depends on NO business module
cas-module-system     Users/roles/auth — depends on infra + thirdparty
cas-module-appointment  Booking/services — depends on system + infra
cas-thirdparty        External integrations (weather, AI chat, OSS, SMS) — depends only on common
cas-server            Boot entry point: aggregates all modules, holds application.yml. No business code.
```

**Dependency direction is enforced by intent, not tooling.** `infra` must not depend on `system`/`appointment`; business modules must not depend on each other directly except via the `system`→`infra`/`thirdparty` chain. `cas-thirdparty` depends only on `cas-common`.

### DDD four-layer layout (every business module)

```
interfaces/      controller (+ dto/, assembler/) — REST boundary
application/     service + impl — orchestration, no business rules, no direct DB
domain/          entity, repository (interfaces)
infrastructure/  persistence (mapper, dataobject), external, aspect
api/             cross-module interface (+ impl) — the ONLY way another module calls this one
```

- Controllers live under `interfaces.controller.admin` for admin endpoints and are guarded with `@RequireRole`.
- `@MapperScan("com.laoliu.cas.**.mapper")` and `@ComponentScan("com.laoliu.cas")` are set once in `CampusAppointmentApplication` (cas-server) — new packages under `com.laoliu.cas` are picked up automatically.
- MyBatis-Plus: mapper XML at `classpath*:/mapper/**/*.xml`, type-aliases package `com.laoliu.cas.**.dataobject`, `map-underscore-to-camel-case: true`.

### Cross-module calls

Modules never call another module's service/Mapper directly. The provider exposes an interface in its `api/` package (e.g. `UserInfoApi`, `GetUserIdViaTokenApi`); the consumer depends on the interface and Spring injects the provider's `@Component` impl. The interface itself often lives in `cas-common` when shared broadly (e.g. `GetUserIdViaTokenApi` in `cas-common.api`, implemented in `cas-module-system`).

### Auth & getting the current user

Stateless JWT. `JWTFilter` (in the security starter) extracts the `Authorization: Bearer <token>` header, validates via `JWTUtils`, and sets a `LoginUser` as the Spring Security principal.

**Do not inject `HttpServletRequest`/`HttpServlet` to read the user.** Use `SecurityFrameworkUtils` (in `cas-common.security`):
- `SecurityFrameworkUtils.getLoginUser()` / `getLoginUserId()` / `getLoginUserRole()` / `isAdmin()` / `isAuthenticated()`

For cross-module "current user id", inject `GetUserIdViaTokenApi` rather than the security utils directly.

Role-based access: annotate controller methods with `@RequireRole(UserRoleEnum.ADMIN)`; enforced by `RoleAspect` in `cas-module-system` infrastructure. Public paths (auth, swagger, `/callTheLargeModel/**`) are permitAll in `SecurityAutoConfiguration`.

### Response envelope

All controllers return `CommonResult<T>` (`cas-common.result`). Use `CommonResult.success(data)` / `CommonResult.error(ErrorCode, params...)`. Throw `BusinessException`/`ForbiddenException`/etc. with an `ErrorCode` from `cas-common.exception.code.*` — `GlobalExceptionHandler` (web starter) converts them to the envelope. Add new error codes to the relevant `*ErrorCode` enum, not inline strings.

## Conventions & anti-patterns

- **cas-common discipline:** only truly cross-cutting code belongs here. Business entities, VOs, and service interfaces belong in their owning module. `plan.md` documents an ongoing migration out of cas-common — when touching cas-common, check whether the class is actually shared or should move to its module.
- No business logic or domain entities in `cas-server` or `cas-common`.
- Domain layer must not depend on Spring annotations; infra implements domain repository interfaces (dependency inversion).
- `application.yml` in cas-server currently contains real credentials (DB, mail, JWT secret, weather API key) checked into git. Treat these as secrets; don't add new ones inline.
- SQL bootstrap scripts live in `sql/` (`database.sql`, `data.sql`, per-table files); Redis data dir is `data/redis`.

## Notes

- Each module has its own `AGENTS.md` and `README.md` with module-specific conventions — read these when working in a module.
- `interact/` and `plan.md` are planning/notes documents (Chinese), not source.
