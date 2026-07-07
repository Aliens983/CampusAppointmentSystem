package com.laoliu.cas.system.interfaces.controller.admin;

import com.laoliu.cas.common.annotation.RequireRole;
import com.laoliu.cas.common.api.GetUserIdViaTokenApi;
import com.laoliu.cas.common.enums.UserRoleEnum;
import com.laoliu.cas.common.result.CommonResult;
import com.laoliu.cas.system.application.service.RoleService;
import com.laoliu.cas.system.domain.entity.User;
import com.laoliu.cas.system.domain.repository.UserRepository;
import com.laoliu.cas.system.interfaces.dto.response.ChangeRoleRespVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员端角色管理接口。
 *
 * @author forever-king
 */
@Tag(name = "用户角色管理（管理）")
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class RoleAdminController {

    private final RoleService roleService;
    private final GetUserIdViaTokenApi getUserIdViaTokenApi;
    private final UserRepository userRepository;

    @Operation(summary = "获取用户角色", description = "获取当前登录用户的角色信息")
    @GetMapping("/role")
    @RequireRole(UserRoleEnum.USER)
    public CommonResult<String> getRole() {
        try {
            Long userId = getUserIdViaTokenApi.getUserId();
            String role = roleService.getRoleByUserId(userId);

            if (role == null) {
                return CommonResult.badRequest("获取用户角色失败");
            }
            return CommonResult.success("获取用户角色成功", role);
        } catch (Exception e) {
            return CommonResult.internalServerError("获取用户角色失败：" + e.getMessage());
        }
    }

    @Operation(summary = "修改用户角色", description = "管理员修改用户角色，返回修改后的用户信息和新的角色（仅限修改非普通用户）")
    @PutMapping("/role")
    @RequireRole(UserRoleEnum.ADMIN)
    public CommonResult<ChangeRoleRespVO> changeRole() {
        try {
            Long userId = getUserIdViaTokenApi.getUserId();
            String role = roleService.getRoleByUserId(userId);

            if ("普通用户".equals(role)) {
                return CommonResult.forbidden("权限不足");
            }

            String changeRole = roleService.changeRoleById(userId);
            if (changeRole == null) {
                return CommonResult.badRequest("角色不存在");
            }

            User user = userRepository.findById(userId).orElse(null);
            return CommonResult.success(ChangeRoleRespVO.of(user, changeRole));
        } catch (Exception e) {
            return CommonResult.internalServerError("修改用户角色失败：" + e.getMessage());
        }
    }
}
