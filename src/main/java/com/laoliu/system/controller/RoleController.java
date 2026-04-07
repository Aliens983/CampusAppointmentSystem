package com.laoliu.system.controller;

import com.laoliu.system.annotation.RequireRole;
import com.laoliu.system.common.exception.enums.RoleErrorCode;
import com.laoliu.system.common.result.CommonResult;
import com.laoliu.system.entity.User;
import com.laoliu.system.common.enums.UserRoleEnum;
import com.laoliu.system.mapper.UserMapper;
import com.laoliu.system.service.RoleService;
import com.laoliu.system.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author 25516
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;
    private final JWTUtils jwtUtils;
    private final UserMapper userMapper;

    public RoleController(RoleService roleService, JWTUtils jwtUtils, UserMapper userMapper) {
        this.roleService = roleService;
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
    }

    @GetMapping
    @RequireRole(UserRoleEnum.USER)
    public CommonResult<String> getRole(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        try {
            if (token != null && token.startsWith("Bearer ")){
                token = token.substring(7);
            }
            Claims claims = jwtUtils.parseToken(token);
            String userId = claims.getSubject();
            String role = roleService.getRoleByUserId(Long.valueOf(userId));

            if (role == null){
                return CommonResult.error(RoleErrorCode.ROLE_NOT_FOUND);
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
        String token = request.getHeader("Authorization");
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Claims claims = jwtUtils.parseToken(token);
            String userId = claims.getSubject();
            String role = roleService.getRoleByUserId(Long.valueOf(userId));
            
            if ("普通用户".equals(role)) {
                return CommonResult.error(RoleErrorCode.PERMISSION_DENIED);
            }

            String changeRole = roleService.changeRoleById(Long.valueOf(userId));
            if (changeRole == null) {
                return CommonResult.error(RoleErrorCode.ROLE_NOT_FOUND);
            }
            
            User user = userMapper.selectByPrimaryKey(Long.valueOf(userId));
            Map<String, Object> result = new HashMap<>();
            result.put("user", user);
            result.put("role", changeRole);
            return CommonResult.success("修改用户角色成功", result);
        } catch (Exception e) {
            return CommonResult.internalServerError("修改用户角色失败：" + e.getMessage());
        }
    }
}
