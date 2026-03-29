package com.laoliu.system.controller;

import com.laoliu.system.annotation.RequireRole;
import com.laoliu.system.entity.User;
import com.laoliu.system.enums.UserRoleEnum;
import com.laoliu.system.mapper.UserMapper;
import com.laoliu.system.service.RoleService;
import com.laoliu.system.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> getRole(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String token = request.getHeader("Authorization");
        try {

            if (token != null && token.startsWith("Bearer ")){
                token = token.substring(7);
            }
            Claims claims = jwtUtils.parseToken(token);

            String userId = claims.getSubject();

            String role = roleService.getRoleByUserId(Long.valueOf(userId));

            if (role == null){
                result.put("没有此用户",false);
                result.put("success",false);

                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }else {
                result.put("success", true);
                result.put("message", "获取用户角色成功");
                result.put("role", role);
                return ResponseEntity.ok(result);
            }
        }catch (Exception e){
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    @Operation(summary = "修改用户角色,并且显示用户信息")
    @RequireRole(UserRoleEnum.ADMIN)
    public ResponseEntity<Map<String, Object>> changeRole(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String token = request.getHeader("Authorization");
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Claims claims = jwtUtils.parseToken(token);
            String userId = claims.getSubject();

            String role = roleService.getRoleByUserId(Long.valueOf(userId));
            if ("普通用户".equals(role)){
                result.put("success", false);
                result.put("message", "普通用户没有权限修改用户角色");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body( result);
            }

            String changeRole = roleService.changeRoleById(Long.valueOf(userId));
            if (changeRole == null){
                result.put("success", false);
                result.put("message", "没有此用户");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }
            result.put("success", true);
            result.put("message", "修改用户角色成功");
            User user = userMapper.selectByPrimaryKey(Long.valueOf(userId));
            result.put("User:", user);
            result.put("role", changeRole);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

}
