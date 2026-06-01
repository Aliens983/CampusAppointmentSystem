package com.laoliu.cas.system.interfaces.controller.admin;

import com.laoliu.cas.common.annotation.RequireRole;
import com.laoliu.cas.common.domain.entity.User;
import com.laoliu.cas.common.enums.UserRoleEnum;
import com.laoliu.cas.common.result.CommonResult;
import com.laoliu.cas.common.util.PasswordUtils;
import com.laoliu.cas.security.util.JWTUtils;
import com.laoliu.cas.common.api.GetUserIdViaTokenApi;
import com.laoliu.cas.system.application.service.UserService;
import com.laoliu.cas.system.infrastructure.persistence.dataobject.UserDO;
import com.laoliu.cas.system.infrastructure.persistence.mapper.UserMapper;
import com.laoliu.cas.system.interfaces.dto.request.AdminCreateUserRequest;
import com.laoliu.cas.system.interfaces.dto.response.UserInfoAndServicesViaMPRespVO;
import com.laoliu.cas.system.interfaces.dto.response.UserResponse;
import com.laoliu.cas.system.interfaces.assembler.UserAssembler;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author forever-king
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "用户接口")
public class UserController {

    private final JWTUtils jwtUtils;
    private final UserMapper userMapper;
    private final UserAssembler userAssembler;
    private final GetUserIdViaTokenApi getUserIdViaTokenApi;
    private final UserService userService;

    @Operation(summary = "获取用户信息")
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
            UserResponse userResponse = userAssembler.convertToUserResponse(user);
            return CommonResult.success(userResponse);
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return CommonResult.internalServerError("获取用户信息失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取所有用户")
    @GetMapping("/all_users")
    @RequireRole(UserRoleEnum.ADMIN)
    public CommonResult<List<UserResponse>> getAllUsers(HttpServletRequest request) {
        try {
            List<UserDO> allUsers = userMapper.getAllUsers();
            List<UserResponse> responses = userAssembler.convertToUserResponseList(allUsers);
            return CommonResult.success(responses);
        } catch (Exception e) {
            log.error("获取所有用户失败", e);
            return CommonResult.internalServerError("获取所有用户失败：" + e.getMessage());
        }
    }

    @Operation(summary = "创建用户")
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
            user.setPassword(PasswordUtils.encode(request.getPassword()));
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
