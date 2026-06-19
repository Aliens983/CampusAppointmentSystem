package com.laoliu.cas.system.application.service.impl;

import com.laoliu.cas.system.application.service.RoleService;
import com.laoliu.cas.system.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author forever-king
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final UserRepository userRepository;

    @Override
    public String changeRoleById(Long userId) {
        String role = userRepository.getRoleByUserId(userId);
        if (role != null) {
            if ("1".equals(role)) {
                userRepository.updateRoleToCommonUser(userId);
                return "普通用户";
            } else if ("0".equals(role)) {
                userRepository.updateRoleToAdmin(userId);
                return "管理员用户";
            }
        }
        return role;
    }

    @Override
    public String getRoleByUserId(Long userId) {
        String role = userRepository.getRoleByUserId(userId);
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
