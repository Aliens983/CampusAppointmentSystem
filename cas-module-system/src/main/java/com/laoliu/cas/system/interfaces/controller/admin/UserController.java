package com.laoliu.cas.system.interfaces.controller.admin;

import com.laoliu.cas.common.annotation.RequireRole;
import com.laoliu.cas.common.api.GetUserIdViaTokenApi;
import com.laoliu.cas.system.domain.entity.User;
import com.laoliu.cas.common.enums.UserRoleEnum;
import com.laoliu.cas.common.result.CommonResult;
import com.laoliu.cas.common.util.PasswordUtils;
import com.laoliu.cas.system.application.service.UserService;
import com.laoliu.cas.system.domain.repository.UserRepository;
import com.laoliu.cas.system.interfaces.assembler.UserAssembler;
import com.laoliu.cas.system.interfaces.dto.request.AdminCreateUserRequest;
import com.laoliu.cas.system.interfaces.dto.response.UserInfoAndServicesViaMPRespVO;
import com.laoliu.cas.system.interfaces.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




/**
 * @author forever-king
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "用户接口")
public class UserController {

    private final UserRepository userRepository;
    private final UserAssembler userAssembler;
    private final GetUserIdViaTokenApi getUserIdViaTokenApi;
    private final UserService userService;

    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的基本信息，包含用户名、邮箱、角色等")
    @GetMapping
    @RequireRole(UserRoleEnum.USER)
    public CommonResult<UserResponse> getUserByParseToken() {
        try {
            Long userId = getUserIdViaTokenApi.getUserId();
            if (userId == null) {
                return CommonResult.unauthorized("用户未登录或登录已过期");
            }
            log.info("获取用户信息成功，用户 ID：{}", userId);
            User user = userRepository.findById(userId).orElse(null);
            log.info("User :{}", user);
            UserResponse userResponse = userAssembler.convertToUserResponse(user);
            return CommonResult.success(userResponse);
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return CommonResult.internalServerError("获取用户信息失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取所有用户列表", description = "管理员获取系统中所有用户的信息列表")
    @GetMapping("/all_users")
    @RequireRole(UserRoleEnum.ADMIN)
    public CommonResult<List<UserResponse>> getAllUsers() {
        try {
            List<User> allUsers = userRepository.getAllUsers();
            List<UserResponse> responses = userAssembler.convertToUserResponseList(allUsers);
            return CommonResult.success(responses);
        } catch (Exception e) {
            log.error("获取所有用户失败", e);
            return CommonResult.internalServerError("获取所有用户失败：" + e.getMessage());
        }
    }

    @Operation(summary = "创建新用户", description = "超级管理员创建新用户，需要提供用户名、邮箱、密码等基本信息")
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

            Long existUserId = userRepository.getUserIdByEmail(request.getEmail());
            if (existUserId != null) {
                return CommonResult.badRequest("该邮箱已被注册");
            }

            User user = User.builder()
                    .name(request.getName()).email(request.getEmail())
                    .password(PasswordUtils.encode(request.getPassword()))
                    .grade(request.getGrade()).sex(request.getSex())
                    .age(request.getAge())
                    .role(request.getRole() != null ? request.getRole() : 0)
                    .build();

            userRepository.save(user);

            return CommonResult.success("创建用户成功");
        } catch (Exception e) {
            return CommonResult.internalServerError("创建用户失败：" + e.getMessage());
        }
    }

    @GetMapping("/get_all_bookings")
    @Operation(summary = "用户查看自己预约的所有服务")
    @RequireRole(UserRoleEnum.USER)
    public CommonResult<UserInfoAndServicesViaMPRespVO> getAllBookings() {
        Long userId = getUserIdViaTokenApi.getUserId();
        return CommonResult.success(userService.getUserInfoAndBookings(userId));
    }
}
