package com.laoliu.cas.system.interfaces.controller;

import com.laoliu.cas.common.annotation.RequireRole;
import com.laoliu.cas.common.domain.entity.User;
import com.laoliu.cas.common.enums.UserRoleEnum;
import com.laoliu.cas.common.result.CommonResult;
import com.laoliu.cas.security.util.JWTUtils;
import com.laoliu.cas.system.application.service.RoleService;
import com.laoliu.cas.system.api.GetUserIdViaTokenApi;
import com.laoliu.cas.system.infrastructure.persistence.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "用户角色管理")
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final JWTUtils jwtUtils;
    private final GetUserIdViaTokenApi getUserIdViaTokenApi;
    private final UserMapper userMapper;

    @Operation(summary = "获取用户角色")
    @GetMapping
    @RequireRole(UserRoleEnum.USER)
    public CommonResult<String> getRole(HttpServletRequest request) {
        try {
            Long userId = getUserIdViaTokenApi.getUserId(request);
            String role = roleService.getRoleByUserId(userId);

            if (role == null) {
                return CommonResult.badRequest("获取用户角色失败");
            }
            return CommonResult.success("获取用户角色成功", role);
        } catch (Exception e) {
            return CommonResult.internalServerError("获取用户角色失败：" + e.getMessage());
        }
    }

    @PutMapping
    @Operation(summary = "修改用户角色，并且显示用户信息")
    @RequireRole(UserRoleEnum.ADMIN)
    public CommonResult<Map<String, Object>> changeRole(HttpServletRequest request) {
        try {
            Long userId = getUserIdViaTokenApi.getUserId(request);
            String role = roleService.getRoleByUserId(userId);

            if ("普通用户".equals(role)) {
                return CommonResult.forbidden("权限不足");
            }

            String changeRole = roleService.changeRoleById(userId);
            if (changeRole == null) {
                return CommonResult.badRequest("角色不存在");
            }

            User user = userMapper.selectByPrimaryKey(userId);
            Map<String, Object> result = new HashMap<>();
            result.put("user", user);
            result.put("role", changeRole);
            return CommonResult.success("修改用户角色成功", result);
        } catch (Exception e) {
            return CommonResult.internalServerError("修改用户角色失败：" + e.getMessage());
        }
    }
}
