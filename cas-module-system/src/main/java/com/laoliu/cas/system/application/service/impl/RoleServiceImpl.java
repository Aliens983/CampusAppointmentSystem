package com.laoliu.cas.system.application.service.impl;

import com.laoliu.cas.system.application.service.RoleService;
import com.laoliu.cas.system.infrastructure.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final UserMapper userMapper;

    @Override
    public String changeRoleById(Long userId) {
        String role = userMapper.getRoleByUserId(userId);
        if (role != null) {
            if ("1".equals(role)) {
                userMapper.updateRoleToCommonUser(userId);
                return "普通用户";
            } else if ("0".equals(role)) {
                userMapper.updateRoleToAdmin(userId);
                return "管理员用户";
            }
        }
        return role;
    }

    @Override
    public String getRoleByUserId(Long userId) {
        String role = userMapper.getRoleByUserId(userId);
        if (role != null) {
            if ("1".equals(role)) {
                return "管理员";
            } else if ("0".equals(role)) {
                return "普通用户";
            }
        }
        return role;
    }
}
