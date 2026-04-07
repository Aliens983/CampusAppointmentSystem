package com.laoliu.system.controller;

import com.laoliu.system.annotation.RequireRole;
import com.laoliu.system.common.exception.enums.ServiceErrorCode;
import com.laoliu.system.common.result.CommonResult;
import com.laoliu.system.entity.Services;
import com.laoliu.system.entity.User;
import com.laoliu.system.common.enums.UserRoleEnum;
import com.laoliu.system.mapper.ItemMapper;
import com.laoliu.system.mapper.ServiceMapper;
import com.laoliu.system.mapper.UserMapper;
import com.laoliu.system.utils.JWTUtils;
import com.laoliu.system.vo.request.ServiceAddRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author 25516
 */
@RestController
@RequestMapping("/service")
public class ServiceController {

    private final ServiceMapper serviceMapper;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;
    private final JWTUtils jwtUtils;

    public ServiceController(ServiceMapper serviceMapper, ItemMapper itemMapper, UserMapper userMapper, JWTUtils jwtUtils) {
        this.serviceMapper = serviceMapper;
        this.itemMapper = itemMapper;
        this.userMapper = userMapper;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping
    public CommonResult<List<Services>> getService() {
        try {
            List<Services> services = serviceMapper.selectAll();
            List<Services> enabledServices = services.stream()
                    .filter(s -> s.getServiceState() == 1)
                    .toList();
            return CommonResult.success(enabledServices);
        } catch (Exception e) {
            return CommonResult.internalServerError("获取服务失败：" + e.getMessage());
        }
    }

    @PostMapping
    @RequireRole(UserRoleEnum.ADMIN)
    public CommonResult<Void> addService(@RequestBody ServiceAddRequest serviceAddRequest) {
        try {
            int rowsAffected = serviceMapper.insertSelective(serviceAddRequest);
            if (rowsAffected > 0) {
                return CommonResult.success("添加服务成功", null);
            } else {
                return CommonResult.error(ServiceErrorCode.SERVICE_BOOK_FAILED);
            }
        } catch (Exception e) {
            return CommonResult.internalServerError("添加服务失败：" + e.getMessage());
        }
    }

    @GetMapping("/id")
    @Operation(summary = "获取指定用户的所有已经预约的服务")
    @RequireRole(UserRoleEnum.ADMIN)
    public CommonResult<Map<String, Object>> getUserServices(HttpServletRequest request, @RequestParam Long userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return CommonResult.error(ServiceErrorCode.SERVICE_NOT_FOUND);
        }
        try {
            List<Services> services = itemMapper.selectUserServices(userId);
            Map<String, Object> result = new HashMap<>();
            result.put("user", user.getName());
            result.put("userId", userId);
            result.put("userRole", user.getRole());
            result.put("userGrade", user.getEmail());
            result.put("services", services);
            return CommonResult.success("获取用户服务成功", result);
        } catch (Exception e) {
            return CommonResult.internalServerError("获取用户服务失败：" + e.getMessage());
        }
    }
}
