package com.laoliu.cas.system.interfaces.controller.admin;

import com.laoliu.cas.common.annotation.RequireRole;
import com.laoliu.cas.common.api.GetUserIdViaTokenApi;
import com.laoliu.cas.common.enums.UserRoleEnum;
import com.laoliu.cas.common.result.CommonResult;
import com.laoliu.cas.system.application.service.RoleService;
import com.laoliu.cas.system.domain.entity.User;
import com.laoliu.cas.system.domain.repository.UserRepository;
import com.laoliu.cas.system.interfaces.dto.request.ChangeRoleRequest;
import com.laoliu.cas.system.interfaces.dto.response.ChangeRoleRespVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Operation(summary = "修改用户角色", description = "管理员修改指定用户的角色，需要提供用户ID和新角色值（0=普通用户, 1=管理员）")
    @PutMapping("/role")
    @RequireRole({UserRoleEnum.ADMIN, UserRoleEnum.SUPER_ADMIN})
    public CommonResult<String> changeRole(@RequestBody ChangeRoleRequest request) {
        try {
            Long targetUserId = request.getUserId();
            Integer newRole = request.getRole();

            if (targetUserId == null) {
                return CommonResult.badRequest("用户ID不能为空");
            }
            if (newRole == null || (newRole != 0 && newRole != 1)) {
                return CommonResult.badRequest("角色值必须为0（普通用户）或1（管理员）");
            }

            User user = userRepository.findById(targetUserId).orElse(null);
            if (user == null) {
                return CommonResult.notFound("用户不存在");
            }

            // 超级管理员不能被降级
            if (user.getRole() != null && user.getRole() == 2) {
                return CommonResult.forbidden("不能修改超级管理员的角色");
            }

            if (newRole == 1) {
                userRepository.updateRoleToAdmin(targetUserId);
            } else {
                userRepository.updateRoleToCommonUser(targetUserId);
            }

            String roleName = newRole == 1 ? "管理员" : "普通用户";
            return CommonResult.success("已将用户 " + user.getName() + " 的角色修改为 " + roleName, roleName);
        } catch (Exception e) {
            return CommonResult.internalServerError("修改用户角色失败：" + e.getMessage());
        }
    }
}
