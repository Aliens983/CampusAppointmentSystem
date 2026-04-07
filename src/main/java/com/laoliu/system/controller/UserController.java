package com.laoliu.system.controller;

import com.laoliu.system.annotation.RequireRole;
import com.laoliu.system.common.result.CommonResult;
import com.laoliu.system.converter.UserConverter;
import com.laoliu.system.entity.User;
import com.laoliu.system.common.enums.UserRoleEnum;
import com.laoliu.system.mapper.UserMapper;
import com.laoliu.system.utils.JWTUtils;
import com.laoliu.system.vo.response.UserResponse;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author 25516
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    private final JWTUtils jwtUtils;
    private final UserMapper userMapper;
    private final UserConverter userConverter;

    public UserController(JWTUtils jwtUtils, UserMapper userMapper, UserConverter userConverter) {
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
        this.userConverter = userConverter;
    }

    @GetMapping
    @RequireRole(UserRoleEnum.USER)
    public CommonResult<UserResponse> getUserByParseToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Claims claims = jwtUtils.parseToken(token);
            String userId = claims.getSubject();
            log.info("Token 解析成功，用户 ID：{}", userId);
            User user = userMapper.selectByPrimaryKey(Long.valueOf(userId));
            log.info("User :{}", user);
            UserResponse userResponse = userConverter.convertUserToUserResponse(user);
            return CommonResult.success(userResponse);
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return CommonResult.internalServerError("获取用户信息失败：" + e.getMessage());
        }
    }

    @GetMapping("/all_users")
    @RequireRole(UserRoleEnum.ADMIN)
    public CommonResult<List<UserResponse>> getAllUsers(HttpServletRequest request) {
        try {
            return CommonResult.success(userMapper.getAllUsers());
        } catch (Exception e) {
            log.error("获取所有用户失败", e);
            return CommonResult.internalServerError("获取所有用户失败：" + e.getMessage());
        }
    }
}
