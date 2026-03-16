package com.laoliu.system.controller;

import com.laoliu.system.entity.Service;
import com.laoliu.system.entity.User;
import com.laoliu.system.mapper.ItemMapper;
import com.laoliu.system.mapper.ServiceMapper;
import com.laoliu.system.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 25516
 */
@RestController
@RequestMapping("/service")
public class ServiceController {

    private final ServiceMapper serviceMapper;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    public ServiceController(ServiceMapper serviceMapper, ItemMapper itemMapper, UserMapper userMapper) {
        this.serviceMapper = serviceMapper;
        this.itemMapper = itemMapper;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getService() {
        try {
            Map<String, Object> result = new HashMap<>();
            List<Service> services = serviceMapper.selectAll();
            result.put("services", services);
            result.put("success", true);
            result.put("message", "获取服务成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "获取服务失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addService(@RequestBody Service service) {
        try {
            Map<String, Object> result = new HashMap<>();
            int rowsAffected = serviceMapper.insertSelective(service);
            if (rowsAffected > 0) {
                result.put("success", true);
                result.put("message", "添加服务成功");
            } else {
                result.put("success", false);
                result.put("message", "添加服务失败");
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();

            Map<String, Object> result = new HashMap<>();

            result.put("success", false);
            result.put("message", "添加服务失败");
            result.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @GetMapping("/id")
    @Operation(summary = "获取指定用户的所有已近预约的服务")
    public ResponseEntity<Map<String, Object>> getUserServices(HttpServletRequest request, @RequestParam Long userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        try {
            if (user == null) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "用户不存在");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }

            Map<String, Object> result = new HashMap<>();
            List<Service> services = itemMapper.selectUserServices(userId);


            result.put("user:", user.getName() );
            result.put("userId", userId);
            result.put("userRole", user.getRole());
            result.put("userGrade", user.getEmail());


            result.put("services", services);
            result.put("success", true);
            result.put("message", "获取用户服务成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "获取用户服务失败");
            result.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
}
