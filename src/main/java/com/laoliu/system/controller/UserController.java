package com.laoliu.system.controller;

import com.laoliu.system.annotation.RequireRole;
import com.laoliu.system.api.GetUserIdViaTokenApi;
import com.laoliu.system.common.result.CommonResult;
import com.laoliu.system.converter.UserConverter;
import com.laoliu.system.entity.User;
import com.laoliu.system.enums.UserRoleEnum;
import com.laoliu.system.mapper.UserMapper;
import com.laoliu.system.service.UserService;
import com.laoliu.system.utils.JWTUtils;
import com.laoliu.system.utils.PasswordUtils;
import com.laoliu.system.vo.request.AdminCreateUserRequest;
import com.laoliu.system.vo.response.UserInfoAndServicesViaMPRespVO;
import com.laoliu.system.vo.response.UserResponse;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private final PasswordUtils passwordUtils;
    private final GetUserIdViaTokenApi getUserIdViaTokenApi;
    private final UserService userService;

    public UserController(JWTUtils jwtUtils, UserMapper userMapper, UserConverter userConverter, PasswordUtils passwordUtils, GetUserIdViaTokenApi getUserIdViaTokenApi, User user, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
        this.userConverter = userConverter;
        this.passwordUtils = passwordUtils;
        this.getUserIdViaTokenApi = getUserIdViaTokenApi;
        this.userService = userService;
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

    @PostMapping("/create")
    @RequireRole(UserRoleEnum.SUPER_ADMIN)
    public CommonResult<String> createUser(@RequestBody AdminCreateUserRequest request) {
        try {
            if (request.getName() == null || request.getName().isEmpty()) {
                return CommonResult.badRequest("用户名不能为空");
            }
            if (request.getEmail() == null || request.getEmail().isEmpty()) {
                return CommonResult.badRequest("邮箱不能为空");
            }
            if (request.getPassword() == null || request.getPassword().isEmpty()) {
                return CommonResult.badRequest("密码不能为空");
            }

            Long existUserId = userMapper.getUserIdByEmail(request.getEmail());
            if (existUserId != null) {
                return CommonResult.badRequest("该邮箱已被注册");
            }

            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordUtils.encode(request.getPassword()));
            user.setGrade(request.getGrade());
            user.setSex(request.getSex());
            user.setAge(request.getAge());
            user.setRole(request.getRole() != null ? request.getRole() : 0);

            userMapper.insert(user);

            return CommonResult.success("创建用户成功");
        } catch (Exception e) {
            return CommonResult.internalServerError("创建用户失败：" + e.getMessage());
        }
    }

    @GetMapping("/get_all_bookings")
    @Operation(summary = "用户查看自己预约的所有服务")
    public CommonResult<UserInfoAndServicesViaMPRespVO> getAllBookings(HttpServletRequest request) {
        Long userId = getUserIdViaTokenApi.getUserId(request);
        return CommonResult.success(userService.getUserInfoAndBookings(userId));
    }
}
